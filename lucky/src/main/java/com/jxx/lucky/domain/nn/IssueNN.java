package com.jxx.lucky.domain.nn;

import com.alibaba.cola.exception.ExceptionFactory;
import com.jxx.lucky.domain.*;
import com.jxx.lucky.param.BetParam;
import lombok.Data;
import lombok.Synchronized;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Data
public class IssueNN extends Issue {
    /**
     * 总税收（开奖之后计算）
     */
    private Integer tax;

    private NNBanker banker;

    List<BetType> hitBets;

    private static List<BetTypeEnum> snBetTypes = Arrays.asList(
            BetTypeEnum.NUMBER_0, BetTypeEnum.NUMBER_1, BetTypeEnum.NUMBER_2, BetTypeEnum.NUMBER_3, BetTypeEnum.NUMBER_4,
            BetTypeEnum.NUMBER_5, BetTypeEnum.NUMBER_6, BetTypeEnum.NUMBER_7, BetTypeEnum.NUMBER_8, BetTypeEnum.NUMBER_9
    );

    public IssueNN() {
        this.state = IssueStateEnum.BETTING;
        this.playerMap = new HashMap<>();
        this.bankerMap = new HashMap<>();
        this.topBetMap = new HashMap<>();

        this.betMap = new HashMap<BetTypeEnum, Integer>(){{
            put(BetTypeEnum.BET_1, 0);
            put(BetTypeEnum.BET_2, 0);
            snBetTypes.forEach(betType -> put(betType, 0));
        }};
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


    @Synchronized
    @Override
    public void becomeBanker(BankerTypeEnum bankerType, Player player) {
        if (!BankerTypeEnum.NN.equals(bankerType) && !BankerTypeEnum.SN.equals(bankerType)) {
            throw ExceptionFactory.bizException("S_ISSUE_BANKER_TYPE_ERROR","庄家类型错误");
        }

        if (bankerMap.get(bankerType) != null) {
            throw ExceptionFactory.bizException("S_ISSUE_ALREADY_HAS_BANKER","已有玩家坐庄");
        }

        if(isBanker(player)) {
            throw ExceptionFactory.bizException("S_ISSUE_ALREADY_BANKER", "已经是庄家");
        }

        Banker banker;
        if (bankerType.equals(BankerTypeEnum.NN)) {
            banker = new NNBanker();
            banker.setUserId(player.getId());
            banker.setMoney(player.getMoney());
            banker.setType(bankerType);
            bankerMap.put(bankerType, banker);
            updateTop(banker);
        } else {
            banker = new SNBanker();
            banker.setUserId(player.getId());
            banker.setType(bankerType);
            banker.setMoney(player.getMoney());
            bankerMap.put(bankerType, banker);
            updateTop(banker);
        }
    }

    private void updateTop(Banker banker) {
        if (banker.getType().equals(BankerTypeEnum.NN)) {
            topBetMap.put(BetTypeEnum.BET_1, banker.getMoney()/2);
            topBetMap.put(BetTypeEnum.BET_2, banker.getMoney()/2);
        } else {
            snBetTypes.forEach(betType -> {
                topBetMap.put(betType, banker.getMoney()/9);
            });
        }
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
        boolean hasNoBankerBet
                = bets.stream().anyMatch(bet -> bankerMap.get(getBankTypeFromBetType(bet.getBetType())) == null);
        if (hasNoBankerBet) {
            throw ExceptionFactory.bizException("S_ISSUE_NO_BANKER", "没有庄家，不能下注");
        }

        // 检查庄家额度
        HashMap<BetTypeEnum, Integer> betMapTmp = new HashMap<>(betMap);
        bets.forEach(betParam -> {
            betMapTmp.merge(betParam.getBetType(), betParam.getAmount(), Integer::sum);
        });
        Map<BetTypeEnum, Integer> topBet = getTop(betMapTmp);
        for (BetParam betParam : bets) {
            if (topBet.get(betParam.getBetType()).compareTo(betParam.getAmount()) < 0) {
                throw ExceptionFactory.bizException(
                        "S_ISSUE_NOT_ENOUGH_TOP_BET",
                        GameConstant.bankerTypeNameMap.get(getBankTypeFromBetType(betParam.getBetType())) + "庄家额度不足");
            }
        }

        playerMap.putIfAbsent(player.getId(), player);
        bets.forEach(betParam -> {
            player.bet(betParam.getAmount(), betParam.getBetType(), betNo);
        });
        this.betMap = betMapTmp;
        this.topBetMap = topBet;
    }

    private BankerTypeEnum getBankTypeFromBetType(BetTypeEnum betType) {
        if (betType.equals(BetTypeEnum.BET_1) || betType.equals(BetTypeEnum.BET_2)) {
            return BankerTypeEnum.NN;
        } else {
            return BankerTypeEnum.SN;
        }
    }

    private Map<BetTypeEnum, Integer> getTop(HashMap<BetTypeEnum, Integer> betMap) {
        Map<BetTypeEnum, Integer> topBetMap = new HashMap<>();

        // 扫牛
        betMap.forEach((betType, amount) -> {
            if (betType.equals(BetTypeEnum.BET_1) || betType.equals(BetTypeEnum.BET_2)) {
                return;
            }

            List<BetTypeEnum> relatedBetTypes = snBetTypes;
            relatedBetTypes.forEach(topBetType -> {
                Integer otherTotalBet = relatedBetTypes.stream()
                        .filter(relatedBetType -> !relatedBetType.equals(topBetType))
                        .reduce(0, (sum, otherBetType) -> sum + betMap.get(otherBetType), Integer::sum);
                Banker banker = bankerMap.get(BankerTypeEnum.SN);
                if (banker == null) {
                    return;
                }
                // 最大可下注额 = (其他区域下注总额 + 庄家身上的钱)/(赔率-1) - 已下注额
                Integer maxBet = (otherTotalBet + banker.getMoney())/9 - betMap.get(topBetType);
                topBetMap.put(topBetType, maxBet);
            });
        });

        if (bankerMap.get(BankerTypeEnum.NN) == null) {
            return topBetMap;
        }

        // 牛牛
        int bankerMoney = bankerMap.get(BankerTypeEnum.NN).getMoney()/2;
        topBetMap.put(
                BetTypeEnum.BET_1,
                bankerMoney - (betMap.get(BetTypeEnum.BET_1) + betMap.get(BetTypeEnum.BET_2)));
        topBetMap.put(
                BetTypeEnum.BET_2,
                bankerMoney - (betMap.get(BetTypeEnum.BET_1) + betMap.get(BetTypeEnum.BET_2)));
        return topBetMap;
    }

    @Override
    public void unBet(Player player, BetTypeEnum type) {

    }
}
