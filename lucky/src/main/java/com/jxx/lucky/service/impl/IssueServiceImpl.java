package com.jxx.lucky.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.cola.exception.ExceptionFactory;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jxx.lucky.config.IssueGameProperty;
import com.jxx.lucky.domain.*;
import com.jxx.lucky.domain.nn.IssueNN;
import com.jxx.lucky.dos.BankerRecordDO;
import com.jxx.lucky.dos.BetRecordDO;
import com.jxx.lucky.dos.IssueDO;
import com.jxx.lucky.event.*;
import com.jxx.lucky.mapper.BankerRecordMapper;
import com.jxx.lucky.mapper.BetMapper;
import com.jxx.lucky.mapper.IssueMapper;
import com.jxx.lucky.param.BetParam;
import com.jxx.lucky.service.IssueService;
import com.jxx.lucky.service.RobotService;
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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    BetMapper betMapper;
    IssueMapper issueMapper;
    BankerRecordMapper bankerRecordMapper;

    Map<BankerTypeEnum, ConcurrentLinkedQueue<Player>> bankerQueueMap;

    List<Banker> offBankers;

    @Value("${banker-min-money}")
    Integer bankerMinMoney;

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

        currentIssue = new IssueNN();
        currentIssue.setIssueNo(DateUtil.format(DateUtil.date(), "MMddHHmm"));
        if(issueMapper.selectById(currentIssue.getIssueNo()) == null) {
            IssueDO issueDO = new IssueDO();
            issueDO.setIssueNo(currentIssue.getIssueNo());
            issueDO.setState(IssueStateEnum.BETTING);
            issueMapper.insert(issueDO);
        }
    }

    @PostConstruct
    public void initGame() {
        log.debug("gameConfig {}, bankerMinMoney {}", gameConfig, bankerMinMoney);
        currentIssue.buildIssue(gameConfig.getGameConfig());

        gameConfig.getGameConfig().forEach(gameConfig -> {
            Player robot = robotService.createRobot();
            currentIssue.becomeBanker(gameConfig.getBankType(), robotService.createRobot());
            log.debug("发送消息成为庄家消息");
            applicationEventPublisher.publishEvent(
                    new BecameBankerEvent(gameConfig.getBankType().name(), robot.getMoney(), robot.getId())
            );
        });
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
            betRecordDO.setMoney(bet.getAmount());
            betRecordDO.setPlayerId(playerId);
            betRecordDO.setState(BetStateEnum.ONGOING);
            betRecordDO.setBetNo(betNo);
            betMapper.insert(betRecordDO);
        }
        currentIssue.bet(player, bets, betNo);

        Integer totalBetAmount = bets.stream().reduce(0, (sum, bet) -> sum + bet.getAmount(), Integer::sum);
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
                    "上庄金额不足，最低上庄金额为" + bankerMinMoney);
        }

        if (player.getMoney().compareTo(money) < 0) {
            throw ExceptionFactory.bizException("BIZ_LUCKY_NOT_ENOUGH_MONEY", "额度不足");
        }
        player.setMoney(money);
        becomeBanker(player, bankerType, currentIssue);

        applicationEventPublisher.publishEvent(
                new BecameBankerEvent(bankerType.name(), player.getMoney(), player.getId())
        );
    }

    private void becomeBanker(Player player, BankerTypeEnum bankerType,  IssueNN issue) {
        log.debug("上庄: player={}, bankerType={} issueNo={}", player, bankerType, issue.getIssueNo());
        // 已有人上庄则进入上庄队列
        Banker currentBanker = issue.getBanker(bankerType);
        if (currentBanker != null) {
            bankerQueueMap.get(bankerType).add(player);
            log.debug("进入等待队列：player={}, bankerType={}", player, bankerType);
            applicationEventPublisher.publishEvent(
                    new WaitBecomeBankerEvent(bankerType.name(), player.getMoney(), player.getId())
            );

            // 如果当前是机器人上庄，则随机2-5期下庄
            if(robotService.isRobot(currentBanker.getUserId())) {
                log.debug("机器人准备下庄：{}", currentBanker);
                robotService.prepareOff(currentBanker.getUserId());
            }
            return;
        }

        // 保存庄家数据
        BankerRecordDO bankerRecordDO = new BankerRecordDO();
        bankerRecordDO.setBankerType(bankerType);
        bankerRecordDO.setIssueNo(issue.getIssueNo());
        bankerRecordDO.setPlayerId(player.getId());
        bankerRecordDO.setAmount(player.getMoney());
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

        // 庄家处理
        Map<BankerTypeEnum, Banker> currentBankerMap = getCurrentBanker();
        currentBankerMap.forEach((bankerType, currentBanker) -> {
            boolean isOff = offBankers.stream().anyMatch(
                    offBank -> offBank.getUserId().equals(currentBanker.getUserId()));
            // 主动下庄或钱不够下庄 topBet + result 是下一期续庄的分
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
                // 续庄
                Player player = new Player();
                player.setId(currentBanker.getUserId());
                player.setMoney(nextIssueTopBet);
                becomeBanker(player, bankerType, nextIssue);
            }
        });

        currentIssue = nextIssue;

        IssueDO issueDO = new IssueDO();
        issueDO.setIssueNo(nextIssue.getIssueNo());
        issueDO.setState(IssueStateEnum.BETTING);
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


    @Transactional(rollbackFor = Exception.class)
    protected void saveIssue(Issue currentIssue, String[] points) {
        // 如果保存错误，在异常处理中将所有投注记录设置为失效（INIT，OPENED, FAIL）
        saveIssueResult(points);
        savePlayerBets();
        saveBanker(getCurrentBanker().values());
    }

    private void saveBanker(Collection<Banker> bankers) {
        bankers.forEach(banker -> {
            BankerRecordDO bankerRecordDO = new BankerRecordDO();
            bankerRecordDO.setBankerType(banker.getType());
            bankerRecordDO.setId(banker.getUserId());
            bankerRecordDO.setIssueNo(currentIssue.getIssueNo());
            bankerRecordDO.setAmount(banker.getResult());
        });
    }

    private void savePlayerBets() {
        Map<Long, Player> playerMap = currentIssue.getPlayerMap();
        playerMap.forEach((id, player) -> {
            List<Bet> bets = player.getBets();
            bets.forEach(bet -> {
                UpdateWrapper<BetRecordDO> betRecordDOUpdateWrapper = new UpdateWrapper<>();
                betRecordDOUpdateWrapper.lambda()
                        .eq(BetRecordDO::getIssueNo, currentIssue.getIssueNo())
                        .eq(BetRecordDO::getBetNo, bet.getBetNo())
                        .set(BetRecordDO::getResult, bet.getResult())
                        .eq(BetRecordDO::getState, BetStateEnum.SETTLED);
            });
        });
    }

    private void saveIssueResult(String[] points) {
        UpdateWrapper<IssueDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .eq(IssueDO::getIssueNo, currentIssue.getIssueNo())
                .set(IssueDO::getPoints, String.join(",", points));
        issueMapper.update(null, updateWrapper);
    }

    /**
     * 先从当前期中查找，不存在的话，从数据库加载
     * @param id 玩家id
     * @return 玩家对象
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
