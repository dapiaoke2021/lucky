package com.jxx.lucky.domain.nn;

import com.alibaba.cola.exception.ExceptionFactory;
import com.jxx.lucky.domain.*;
import com.jxx.lucky.param.BetParam;
import lombok.Data;
import lombok.Synchronized;

import java.util.*;

@Data
public class IssueNN extends Issue {
    /**
     * 总税收（开奖之后计算）
     */
    private Integer tax;

    private NNBanker banker;

    List<BetType> hitBets;

    public IssueNN() {
        this.state = IssueStateEnum.BETTING;
        this.playerMap = new HashMap<>();
    }

    @Synchronized
    public void becomeBanker(Player player){
        if (banker != null) {
            throw ExceptionFactory.bizException("S_ISSUE_ALREADY_HAS_BANKER","已有玩家坐庄");
        }

        if(isBanker(player)) {
            throw ExceptionFactory.bizException("S_ISSUE_ALREADY_BANKER", "已经是庄家");
        }

        NNBanker banker = new NNBanker();
        banker.setUserId(player.getId());
        banker.setTopBet(player.getMoney());
        this.banker = banker;
        this.bankerMap.put(BankerTypeEnum.NN, banker);
    }

    private boolean isBanker(Player player) {
        return banker != null && banker.getUserId().equals(player.getId());
    }

    public void open(String bankerPoint, String xianPoint1, String xianPoint2) {
        NNGameResultType bankerResult = new NNGameResultType(bankerPoint);
        NNGameResultType xianResult1 = new NNGameResultType(xianPoint1);
        NNGameResultType xianResult2 = new NNGameResultType(xianPoint2);

        List<BetTypeResult> bankerBetTypeResults = new ArrayList<>();
        List<BetTypeResult> playerBetTypeResults = new ArrayList<>();
        if (bankerResult.compareTo(xianResult1) >= 0) {
            bankerBetTypeResults.add(new BetTypeResult(BetTypeEnum.BET_1, true, bankerResult));
            playerBetTypeResults.add(new BetTypeResult(BetTypeEnum.BET_1, false, bankerResult));
        } else {
            bankerBetTypeResults.add(new BetTypeResult(BetTypeEnum.BET_1, false, xianResult1));
            playerBetTypeResults.add(new BetTypeResult(BetTypeEnum.BET_1, true, xianResult1));
        }

        if (bankerResult.compareTo(xianResult2) >= 0) {
            bankerBetTypeResults.add(new BetTypeResult(BetTypeEnum.BET_2, true, bankerResult));
            playerBetTypeResults.add(new BetTypeResult(BetTypeEnum.BET_2, false, bankerResult));
        } else {
            bankerBetTypeResults.add(new BetTypeResult(BetTypeEnum.BET_2, false, xianResult2));
            playerBetTypeResults.add(new BetTypeResult(BetTypeEnum.BET_2, true, xianResult2));
        }

        bankerMap.forEach((bankerType, banker) -> banker.open(betMap, bankerBetTypeResults));
        playerMap.forEach((id, player) -> player.open(playerBetTypeResults));
    }


    @Override
    public void becomeBanker(BankerTypeEnum bankerType, Player player) {
        if (!BankerTypeEnum.NN.equals(bankerType)) {
            throw ExceptionFactory.bizException("S_ISSUE_BANKER_TYPE_ERROR","庄家类型错误");
        }

        if (bankerMap.get(bankerType) != null) {
            throw ExceptionFactory.bizException("S_ISSUE_ALREADY_HAS_BANKER","已有玩家坐庄");
        }

        if(isBanker(player)) {
            throw ExceptionFactory.bizException("S_ISSUE_ALREADY_BANKER", "已经是庄家");
        }

        NNBanker banker = new NNBanker();
        banker.setUserId(player.getId());
        banker.setTopBet(player.getMoney()/3);
        banker.setType(bankerType);
        bankerMap.put(bankerType, banker);
    }

    @Override
    public void open(String[] points) {
        open(points[2], points[3], points[4]);
    }

    @Override
    public List<BetType> getHitBets() {
        return hitBets;
    }

    @Override
    public void bet(Player player, List<BetParam> bets, String betNo) {
        if (!state.equals(IssueStateEnum.BETTING)) {
            throw ExceptionFactory.bizException("S_ISSUE_OUT_BET_TIME", "下注时间已过");
        }

        // 检查玩家金额
        if(!player.check(bets)) {
            throw ExceptionFactory.bizException("S_PLAYER_NOT_ENOUGH_GOLD", "金额不足");
        }

        // 检查庄家是否存在
        if (banker == null) {
            throw ExceptionFactory.bizException("S_ISSUE_NO_BANKER", "没有庄家，不能下注");
        }

        // 检查庄家额度
        Integer totalBet = bets.stream().reduce(0, (sum, bet) -> sum + bet.getAmount(), Integer::sum);
        Integer topBet = banker.getTopBet();
        if (topBet.compareTo(totalBet) < 0) {
            throw ExceptionFactory.bizException("S_ISSUE_NOT_ENOUGH_TOP_BET", "庄家额度不足");
        }

        playerMap.putIfAbsent(player.getId(), player);
        bets.forEach(betParam -> {
            player.bet(betParam.getAmount(), betParam.getBetType(), betNo);
        });

        bets.forEach(betParam -> {
            this.betMap.merge(betParam.getBetType(), betParam.getAmount(), Integer::sum);
        });
    }

    @Override
    public void unBet(Player player, BetTypeEnum type) {

    }
}
