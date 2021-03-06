package com.jxx.lucky.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.cola.exception.ExceptionFactory;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.binance.client.model.market.Candlestick;
import com.jxx.lucky.config.IssueGameProperty;
import com.jxx.lucky.domain.*;
import com.jxx.lucky.domain.nn.IssueNN;
import com.jxx.lucky.domain.nn.NNGameResultType;
import com.jxx.lucky.dos.BankerRecordDO;
import com.jxx.lucky.dos.BetRecordDO;
import com.jxx.lucky.dos.IssueDO;
import com.jxx.lucky.dos.ResultDO;
import com.jxx.lucky.event.*;
import com.jxx.lucky.mapper.BankerRecordMapper;
import com.jxx.lucky.mapper.BetMapper;
import com.jxx.lucky.mapper.IssueMapper;
import com.jxx.lucky.param.BetParam;
import com.jxx.lucky.service.IPointGenerator;
import com.jxx.lucky.service.IssueService;
import com.jxx.lucky.service.RobotService;
import com.jxx.lucky.vo.CurrentIssueDataVO;
import com.jxx.user.service.IUserService;
import com.jxx.user.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author a1
 */
@Slf4j
@RefreshScope
@Service
public class IssueServiceImpl implements IssueService {

    private IssueNN currentIssue;

    private final RobotService robotService;

    @Autowired
    IUserService userService;
    @Autowired
    IPointGenerator pointGenerator;

    BetMapper betMapper;
    IssueMapper issueMapper;
    BankerRecordMapper bankerRecordMapper;

    Map<BankerTypeEnum, ConcurrentLinkedQueue<Player>> bankerQueueMap;

    List<Banker> offBankers;

    @Value("${banker-min-money}")
    Integer bankerMinMoney;

    @Value("${point-source}")
    String pointSource;

    @Autowired
    IssueGameProperty gameConfig;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public IssueServiceImpl(BetMapper betMapper,
                            IssueMapper issueMapper,
                            BankerRecordMapper bankerRecordMapper,
                            RobotService robotService) {
        this.betMapper = betMapper;
        this.issueMapper = issueMapper;
        this.bankerRecordMapper = bankerRecordMapper;
        this.robotService = robotService;
        this.offBankers = new ArrayList<>();

        bankerQueueMap = new ConcurrentHashMap<>();


    }

    @PostConstruct
    public void initGame() {
        log.debug("gameConfig {}, bankerMinMoney {}", gameConfig, bankerMinMoney);

        currentIssue = new IssueNN();
        currentIssue.buildIssue(gameConfig.getGameConfig());

        currentIssue.setIssueNo(DateUtil.format(DateUtil.date(), "MMddHHmm"));
        if(issueMapper.selectById(currentIssue.getIssueNo()) == null) {
            IssueDO issueDO = new IssueDO();
            issueDO.setIssueNo(currentIssue.getIssueNo());
            issueDO.setState(IssueStateEnum.BETTING);
            issueDO.setPointSource(pointSource);
            issueMapper.insert(issueDO);
        }

        gameConfig.getGameConfig().forEach(gameConfig -> {
            Player robot = robotService.createRobot();
            currentIssue.becomeBanker(gameConfig.getBankType(), robot);
            applicationEventPublisher.publishEvent(
                    new BecameBankerEvent(gameConfig.getBankType().name(), robot.getMoney(), robot.getId())
            );
        });

        log.debug("????????????:{}", getCurrentBanker());
    }

    @PostConstruct
    public void initBankerQueue() {
        gameConfig.getGameConfig().forEach(gameConfig -> {
            bankerQueueMap.put(gameConfig.getBankType(), new ConcurrentLinkedQueue<>());
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String bet(Long playerId, List<BetParam> bets) {
        Player player = getPlayer(playerId);

        String betNo = playerId.toString() + "-" + DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss");
        for (BetParam bet : bets) {
            BetRecordDO betRecordDO = new BetRecordDO();
            betRecordDO.setBetType(bet.getBetType().ordinal());
            betRecordDO.setIssueNo(currentIssue.getIssueNo());
            betRecordDO.setMoney(player.getMoney());
            betRecordDO.setPlayerId(playerId);
            betRecordDO.setState(BetStateEnum.ONGOING);
            betRecordDO.setBetNo(betNo);
            betRecordDO.setBet(bet.getAmount());
            betRecordDO.setPointSource(pointSource);
            betMapper.insert(betRecordDO);
        }
        currentIssue.bet(player, bets, betNo);

        Integer totalBetAmount = bets.stream().reduce(0, (sum, bet) -> sum + bet.getAmount(), Integer::sum);
        // todo: ??????????????????????????????
        applicationEventPublisher.publishEvent(new BetEvent(playerId, totalBetAmount));
        return betNo;
    }

    @Override
    public void unBet(Long playerId, BetTypeEnum type) {
        Player player = getPlayer(playerId);

        UpdateWrapper<BetRecordDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .eq(BetRecordDO::getPlayerId, player)
                .eq(BetRecordDO::getIssueNo, currentIssue.getIssueNo())
                .eq(BetRecordDO::getBetType, type)
                .set(BetRecordDO::getState, BetStateEnum.REVOKE);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void becomeBanker(Long playerId, BankerTypeEnum bankerType, Integer money) {
        Player player = getPlayer(playerId);
        if (bankerMinMoney.compareTo(money) > 0) {
            throw ExceptionFactory.bizException(
                    "BIZ_LUCKY_MONEY_TOO_LITTLE",
                    "??????????????????????????????????????????" + bankerMinMoney);
        }

        if (player.getMoney().compareTo(money) < 0) {
            throw ExceptionFactory.bizException("BIZ_LUCKY_NOT_ENOUGH_MONEY", "????????????");
        }
        player.setMoney(money);
        becomeBanker(player, bankerType, currentIssue);

        applicationEventPublisher.publishEvent(
                new BecameBankerEvent(bankerType.name(), player.getMoney(), player.getId())
        );
    }

    private void becomeBanker(Player player, BankerTypeEnum bankerType,  IssueNN issue) {
        log.debug("??????: player={}, bankerType={} issueNo={}", player, bankerType, issue.getIssueNo());
        // ????????????????????????????????????
        Banker currentBanker = issue.getBanker(bankerType);
        if (currentBanker != null) {
            bankerQueueMap.get(bankerType).add(player);
            log.debug("?????????????????????player={}, bankerType={}", player, bankerType);
            applicationEventPublisher.publishEvent(
                    new WaitBecomeBankerEvent(bankerType.name(), player.getMoney(), player.getId())
            );

            // ??????????????????????????????????????????2-5?????????
            if(robotService.isRobot(currentBanker.getUserId())) {
                log.debug("????????????????????????{}", currentBanker);
                robotService.prepareOff(currentBanker.getUserId());
            }
            return;
        }

        // ??????????????????
        BankerRecordDO bankerRecordDO = new BankerRecordDO();
        bankerRecordDO.setBankerType(bankerType);
        bankerRecordDO.setIssueNo(issue.getIssueNo());
        bankerRecordDO.setPlayerId(player.getId());
        bankerRecordDO.setMoney(player.getMoney());
        bankerRecordMapper.insert(bankerRecordDO);

        issue.becomeBanker(bankerType, player);
    }

    private <T> void sendMessage(T payload, String tag) {
        applicationEventPublisher.publishEvent(payload);
    }

    @Override
    public Map<BankerTypeEnum, Banker> getCurrentBanker() {
        return gameConfig.getGameConfig().stream()
                .map(gameConfig -> currentIssue.getBanker(gameConfig.getBankType()))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Banker::getType, Function.identity()));
    }

    @Override
    public Map<BankerTypeEnum, ConcurrentLinkedQueue<Player>> getWaitBanker() {
        return bankerQueueMap;
    }

    @Override
    public void offBanker(Long playerId, BankerTypeEnum bankerType) {
        Banker currentBanker = getCurrentBanker().get(bankerType);
        if (currentBanker != null && currentBanker.getUserId().equals(playerId)) {
            offBankers.add(currentBanker);
            return;
        }

        Player player = new Player();
        player.setId(playerId);
        bankerQueueMap.get(bankerType).remove(player);

        applicationEventPublisher.publishEvent(new OffedBankerEvent(player.getId(), currentBanker.getMoney()));
    }

    @Override
    public void open(String[] points) {
        currentIssue.open(points);
        saveIssue(currentIssue, points);

        Map<Long, Integer> result = new HashMap<>();
        Map<Long, Integer> tax = new HashMap<>();
        currentIssue.getPlayerMap().forEach((playerId, player) -> {
            result.put(playerId, player.getBonus());
            tax.put(playerId, player.getTax());
        });
        applicationEventPublisher.publishEvent(new IssueOpenedEvent(currentIssue.getIssueNo(), points, result, tax));
        newIssue();
    }

    private void newIssue() {
        IssueNN nextIssue = new IssueNN();
        nextIssue.buildIssue(gameConfig.getGameConfig());
        nextIssue.setIssueNo(DateUtil.format(DateUtil.date(), "MMddHHmm"));
        log.debug("nextIssue {}", nextIssue);

        // ????????????
        Map<BankerTypeEnum, Banker> currentBankerMap = getCurrentBanker();
        log.debug("????????????:{}", currentBankerMap);
        currentBankerMap.forEach((bankerType, currentBanker) -> {
            boolean isOff = offBankers.stream().anyMatch(
                    offBank -> offBank.getUserId().equals(currentBanker.getUserId()));
            // ?????????????????????????????? topBet + result ????????????????????????
            Integer nextIssueTopBet = currentBanker.getResult() + currentBanker.getMoney();
            if (isOff || nextIssueTopBet.compareTo(bankerMinMoney) < 0) {
                applicationEventPublisher.publishEvent(
                        new OffedBankerEvent(currentBanker.getUserId(), currentBanker.getMoney()));
                Player player = bankerQueueMap.get(bankerType).poll();
                if (player != null) {
                    becomeBanker(player, bankerType, nextIssue);
                    applicationEventPublisher.publishEvent(
                            new BecameBankerEvent(bankerType.name(), player.getMoney(), player.getId())
                    );
                } else {
                    Player robot = robotService.createRobot();
                    becomeBanker(robot, bankerType, nextIssue);
                    applicationEventPublisher.publishEvent(
                            new BecameBankerEvent(bankerType.name(), robot.getMoney(), robot.getId())
                    );
                }
            } else {
                // ??????
                Player player = new Player();
                player.setId(currentBanker.getUserId());
                player.setMoney(nextIssueTopBet);
                log.debug("?????????{}", player);
                becomeBanker(player, bankerType, nextIssue);
            }
        });

        currentIssue = nextIssue;

        IssueDO issueDO = new IssueDO();
        issueDO.setIssueNo(nextIssue.getIssueNo());
        issueDO.setState(IssueStateEnum.BETTING);
        issueDO.setPointSource(pointSource);
        issueMapper.insert(issueDO);

        applicationEventPublisher.publishEvent(
                new CreatedIssueEvent(nextIssue.getIssueNo())
        );
    }

    @Override
    public Map<BetTypeEnum, Integer> getTopBetMap() {
        return currentIssue.getTopBetMap();
    }

    @Override
    public Map<BetTypeEnum, Integer> getBetMap() {
        return currentIssue.getBetMap();
    }

    @Override
    public String getCurrentIssueNo() {
        return currentIssue.getIssueNo();
    }

    @Override
    public void closeBet() {
        currentIssue.setState(IssueStateEnum.SETTLE);
    }

    @Override
    public CurrentIssueDataVO currentIssueData() {
        CurrentIssueDataVO currentIssueDataVO = new CurrentIssueDataVO();
        String currentIssueNo = this.getCurrentIssueNo();
        Candlestick candlestick;
        try{
            candlestick = pointGenerator.getCandlestick(currentIssueNo);
        }catch (Exception e) {
            candlestick = new Candlestick();
            candlestick.setHigh(RandomUtil.randomBigDecimal(BigDecimal.valueOf(30000),BigDecimal.valueOf(40000)).setScale(2,BigDecimal.ROUND_UP));
            candlestick.setLow(RandomUtil.randomBigDecimal(BigDecimal.valueOf(30000),BigDecimal.valueOf(40000)).setScale(2,BigDecimal.ROUND_UP));
            candlestick.setClose(RandomUtil.randomBigDecimal(BigDecimal.valueOf(30000),BigDecimal.valueOf(40000)).setScale(2,BigDecimal.ROUND_UP));
        }
        log.debug("point = {}", JSONUtil.toJsonStr(candlestick));
        currentIssueDataVO.setCurrentIssueNo(currentIssueNo);
        currentIssueDataVO.setHign(candlestick.getHigh());
        currentIssueDataVO.setLow(candlestick.getLow());
        currentIssueDataVO.setClose(candlestick.getClose());
        currentIssueDataVO.setHignPoint(new NNGameResultType(currentIssueDataVO.getHign().toString()).getNiu());
        currentIssueDataVO.setLowPoint(new NNGameResultType(currentIssueDataVO.getLow().toString()).getNiu());
        currentIssueDataVO.setClosePoint(new NNGameResultType(currentIssueDataVO.getClose().toString()).getNiu());

        return currentIssueDataVO;
    }

    @Override
    public void getHistory(int count) {
        QueryWrapper<IssueDO> queryWrapper = new QueryWrapper<>();

    }


    @Transactional(rollbackFor = Exception.class)
    protected void saveIssue(Issue currentIssue, String[] points) {
        // ??????????????????????????????????????????????????????????????????????????????INIT???OPENED, FAIL???
        saveIssueResult(points);
        savePlayerBets();
        saveBanker(getCurrentBanker().values(), points);
    }

    private void saveBanker(Collection<Banker> bankers, String[] points) {
        Map<BetTypeEnum, Integer> betMap = currentIssue.getBetMap();
        bankers.forEach(banker -> {
            if(robotService.isRobot(banker.getUserId())) {
                return;
            }
            UserVO bankerUser = userService.getUser(banker.getUserId());
            Map<BetTypeEnum, Integer> betResult = banker.getBetResult();
            String betNo = banker.getUserId().toString() + "-" + DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss");
            betResult.forEach((betType, amount) -> {
                if (betMap.get(betType).equals(0)) {
                    return;
                }
                BetRecordDO betRecordDO = new BetRecordDO();
                betRecordDO.setBet(betMap.get(betType));
                betRecordDO.setBetType(betType.ordinal());
                betRecordDO.setBankerType(banker.getType().ordinal());
                betRecordDO.setIssueNo(currentIssue.getIssueNo());
                betRecordDO.setBetNo(betNo);
                betRecordDO.setPointSource(pointSource);
                betRecordDO.setState(BetStateEnum.SETTLED);
                betRecordDO.setPlayerId(banker.getUserId());
                betRecordDO.setCreateTime(new Timestamp(System.currentTimeMillis()));
                betRecordDO.setResult(amount);

                // ????????????+????????????
                betRecordDO.setMoney(bankerUser.getMoney() + banker.getMoney());

                betMapper.insert(betRecordDO);
            });
        });
    }

    private void savePlayerBets() {
        Map<Long, Player> playerMap = currentIssue.getPlayerMap();
        log.debug("???{}?????????????????????{}", currentIssue.getIssueNo(), playerMap);
        playerMap.forEach((id, player) -> {
            List<Bet> bets = player.getBets();
            Integer money = userService.getUser(id).getMoney();
            bets.forEach(bet -> {
                UpdateWrapper<BetRecordDO> betRecordDOUpdateWrapper = new UpdateWrapper<>();
                betRecordDOUpdateWrapper.lambda()
                        .eq(BetRecordDO::getIssueNo, currentIssue.getIssueNo())
                        .eq(BetRecordDO::getPointSource, pointSource)
                        .eq(BetRecordDO::getBetNo, bet.getBetNo())
                        .set(BetRecordDO::getResult, bet.getResult())
                        .set(BetRecordDO::getMoney, money)
                        .set(BetRecordDO::getState, BetStateEnum.SETTLED);
                betMapper.update(null, betRecordDOUpdateWrapper);
            });
        });
    }

    private String convert(BetResult betResult) {
        return String.valueOf(betResult.getBetType().ordinal()) + "_" + (betResult.isBankerWin() ? 0 : 1);
    }

    private void saveIssueResult(String[] points) {
        String result = currentIssue.getGameMap().values().stream().map(game -> {
            return game.getBetResults(points).stream().map(this::convert).collect(Collectors.joining(","));
        }).collect(Collectors.joining(","));

        UpdateWrapper<IssueDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .eq(IssueDO::getIssueNo, currentIssue.getIssueNo())
                .eq(IssueDO::getPointSource, pointSource)
                .set(IssueDO::getResult, result)
                .set(IssueDO::getPoints, String.join(",", points));

        issueMapper.update(null, updateWrapper);
    }

    /**
     * ???????????????????????????????????????????????????????????????
     * @param id ??????id
     * @return ????????????
     */
    private Player getPlayer(Long id) {
        Player player = currentIssue.getPlayerMap().get(id);
        if (player == null) {
            UserVO userVO = userService.getUser(id);
            player = new Player();
            player.setId(userVO.getId());
            player.setMoney(userVO.getMoney());
        }

        return player;
    }

}
