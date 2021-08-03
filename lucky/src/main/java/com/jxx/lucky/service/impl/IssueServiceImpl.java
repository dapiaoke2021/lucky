package com.jxx.lucky.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.cola.exception.ExceptionFactory;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jxx.lucky.domain.*;
import com.jxx.lucky.dos.BankerRecordDO;
import com.jxx.lucky.dos.BetRecordDO;
import com.jxx.lucky.dos.IssueDO;
import com.jxx.lucky.mapper.BankerRecordMapper;
import com.jxx.lucky.mapper.BetMapper;
import com.jxx.lucky.mapper.IssueMapper;
import com.jxx.lucky.param.BetParam;
import com.jxx.lucky.service.IssueService;
import com.jxx.lucky.service.RobotService;
import com.jxx.user.vo.UserVO;
import com.jxx.user.service.IUserServiceApi;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author a1
 */
@RefreshScope
@Service
public class IssueServiceImpl implements IssueService {

    private Issue currentIssue;

    private RobotService robotService;

    @Reference
    IUserServiceApi userService;

    BetMapper betMapper;
    IssueMapper issueMapper;
    BankerRecordMapper bankerRecordMapper;

    Map<BankerTypeEnum, ConcurrentLinkedQueue<Player>> bankerQueueMap;

    List<Banker> offBankers;

    @Value("${lucky.banker-min-money}")
    Integer bankerMinMoney;

    //todo: 事件：上庄，下庄，下注，结算。使用spring cloud标准

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

        currentIssue = new Issue();
        currentIssue.setIssueNo(DateUtil.format(DateUtil.date(), "MMddHHmm"));
        bankerQueueMap = new ConcurrentHashMap<>();
        bankerQueueMap.put(BankerTypeEnum.BIG_SMALL, new ConcurrentLinkedQueue<>());
        bankerQueueMap.put(BankerTypeEnum.OOD_EVEN, new ConcurrentLinkedQueue<>());
        bankerQueueMap.put(BankerTypeEnum.NUMBER, new ConcurrentLinkedQueue<>());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String bet(Long playerId, List<BetParam> bets) {
        Player player = getPlayer(playerId);

        String betNo = playerId.toString() + "-" + DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss");
        for (BetParam bet : bets) {
            BetRecordDO betRecordDO = new BetRecordDO();
            betRecordDO.setBetType(bet.getBetType());
            betRecordDO.setIssueNo(currentIssue.getIssueNo());
            betRecordDO.setMoney(bet.getAmount());
            betRecordDO.setPlayerId(playerId);
            betRecordDO.setState(BetStateEnum.ONGOING);
            betRecordDO.setBetNo(betNo);
            betMapper.insert(betRecordDO);
        }
        currentIssue.bet(player, bets);
        // todo: 发送BetEvent

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
        currentIssue.unBet(player, type);
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
    }

    private void becomeBanker(Player player, BankerTypeEnum bankerType,  Issue issue) {
        // 已有人上庄则进入上庄队列
        Banker currentBanker = issue.getBankerMap().get(bankerType);
        if (currentBanker != null) {
            bankerQueueMap.get(bankerType).add(player);

            //todo: WaitBecomeBankerEvent

            // 如果当前是机器人上庄，则随机2-5期下庄
            if(robotService.isRobot(currentBanker.getUserId())) {
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

        // todo: BecameBankerEvent
    }

    @Override
    public Map<BankerTypeEnum, Banker> getCurrentBanker() {
        return currentIssue.getBankerMap();
    }

    @Override
    public Map<BankerTypeEnum, ConcurrentLinkedQueue<Player>> getWaitBanker() {
        return bankerQueueMap;
    }

    @Override
    public void offBanker(Long playerId, BankerTypeEnum bankerType) {
        Banker currentBanker = currentIssue.getBankerMap().get(bankerType);
        if (currentBanker != null && currentBanker.getUserId().equals(playerId)) {
            offBankers.add(currentBanker);
            return;
        }

        Player player = new Player();
        player.setId(playerId);
        bankerQueueMap.get(bankerType).remove(player);

        // todo: OffedBankerEvent
    }

    @Override
    public void open(Integer point) {
        currentIssue.open(point);
        saveIssue(currentIssue);

        // todo: IssueOpenedEvent
        newIssue();
    }

    private void newIssue() {
        Issue nextIssue = new Issue();
        nextIssue.setIssueNo(DateUtil.format(DateUtil.date(), "yyyyMMddHHmm"));

        // 庄家处理
        Map<BankerTypeEnum, Banker> currentBankerMap = currentIssue.getBankerMap();
        currentBankerMap.forEach((bankerType, currentBanker) -> {
            boolean isOff = offBankers.stream().anyMatch(
                    offBank -> offBank.getUserId().equals(currentBanker.getUserId()));
            // 主动下庄或钱不够下庄 topBet + result 是下一期续庄的分
            Integer nextIssueTopBet = currentBanker.getResult() + currentBanker.getTopBet();
            if (isOff || nextIssueTopBet.compareTo(bankerMinMoney) < 0) {
                // todo: currentBanker 发送下庄事件 offedBanker
                Player player = bankerQueueMap.get(bankerType).poll();
                if (player != null) {
                    becomeBanker(player, bankerType, nextIssue);
                } else {
                    Player robot = robotService.createRobot();
                    becomeBanker(robot, bankerType, nextIssue);
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
        issueDO.setIssueNo(DateUtil.format(DateUtil.date(), "MMddHHmm"));
        issueDO.setState(IssueStateEnum.BETTING);
        issueMapper.insert(issueDO);
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
    protected void saveIssue(Issue currentIssue) {
        // 如果保存错误，在异常处理中将所有投注记录设置为失效（INIT，OPENED, FAIL）
        saveIssueResult(currentIssue.getPoint());
        savePlayerBets(currentIssue.getPoint());
        saveBanker(currentIssue.getBankerMap().values());
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

    private void savePlayerBets(Integer point) {
        List<BetType> hitBetTypes = currentIssue.getHitBets(point);
        UpdateWrapper<BetRecordDO> betRecordDOUpdateWrapper = new UpdateWrapper<>();
        betRecordDOUpdateWrapper.lambda()
                .eq(BetRecordDO::getIssueNo, currentIssue.getIssueNo())
                .set(BetRecordDO::getWin, true)
                .eq(BetRecordDO::getState, BetStateEnum.ONGOING)
                .in(BetRecordDO::getBetType, hitBetTypes);
    }

    private void saveIssueResult(Integer point) {
        UpdateWrapper<IssueDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .eq(IssueDO::getIssueNo, currentIssue.getIssueNo())
                .set(IssueDO::getPoint, point);
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
            UserVO userVO = userService.getById(id);
            player = new Player();
            player.setId(userVO.getId());
            player.setMoney(userVO.getMoney());
        }

        return player;
    }
}
