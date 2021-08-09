package com.jxx.lucky.domain.point;

import com.alibaba.cola.exception.ExceptionFactory;
import com.jxx.lucky.domain.*;
import com.jxx.lucky.param.BetParam;
import lombok.Data;
import lombok.Getter;
import lombok.Synchronized;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author a1
 */
@Data
public class IssuePoint extends Issue{

    /**
     * 开奖点数
     */
    @Getter
    private Integer point;

    /**
     * 总税收（开奖之后计算）
     */
    private Integer tax;

    public IssuePoint() {
        this.state = IssueStateEnum.BETTING;
        this.bankerMap = new HashMap<>();

        this.playerMap = new HashMap<>();
        this.topBetMap = new HashMap<>();
        this.betMap = new HashMap<>();
        GameConstant.betTypeMap.keySet().forEach(betType -> {
            this.topBetMap.put(betType, 0);
            this.betMap.put(betType, 0);
        });
    }

    @Synchronized
    public void becomeBanker(BankerTypeEnum bankerType, Player player){
        if (bankerMap.get(bankerType) != null) {
            throw ExceptionFactory.bizException("S_ISSUE_ALREADY_HAS_BANKER","已有玩家坐庄");
        }

        if(isBanker(player)) {
            throw ExceptionFactory.bizException("S_ISSUE_ALREADY_BANKER", "已经是庄家");
        }

        PointGameBanker banker = new PointGameBanker();
        banker.setUserId(player.getId());
        banker.setMoney(player.getMoney());
        banker.setType(bankerType);
        bankerMap.put(bankerType, banker);

        updateTop(bankerType, banker);
    }

    @Override
    public void open(String[] points) {
        String data = points[4];
        open(Integer.parseInt(String.valueOf(data.charAt(data.length() - 1))));
    }

    @Override
    public List<BetType> getHitBets() {
        return getHitBets(this.point);
    }

    private boolean isBanker(Player player) {
        return bankerMap.values().stream().anyMatch(banker -> banker.getUserId().equals(player.getId()));
    }

    public void open(Integer point) {
        this.point = point;
        List<BetType> hitBets = getHitBets(point);
        int playerTotalTax = playerMap.values().stream().reduce(
                0,
                (sum, player) -> sum + 0,// player.open(hitBets),
                Integer::sum
        );
        int bankerTotalTax = bankerMap.values().stream().reduce(
                0,
                (sum, banker) -> sum + 0,//banker.open(betMap, ),
                Integer::sum
        );
        this.tax = playerTotalTax + bankerTotalTax;
    }

    public List<BetType> getHitBets(Integer point) {
        return new ArrayList<>();
    }

    @Synchronized
    public void bet(Player player, List<BetParam> bets, String betNo) {
        if (!state.equals(IssueStateEnum.BETTING)) {
            throw ExceptionFactory.bizException("S_ISSUE_OUT_BET_TIME", "下注时间已过");
        }

        // 检查玩家金额
        if(!player.check(bets)) {
            throw ExceptionFactory.bizException("S_PLAYER_NOT_ENOUGH_GOLD", "金额不足");
        }

        // 检查庄家是否存在
        for (BetParam betParam : bets) {
            BankerTypeEnum bankerType = getBankTypeFromBetType(betParam.getBetType());
            if(bankerMap.get(bankerType) == null) {
                throw ExceptionFactory.bizException(
                        "S_ISSUE_NO_BANKER",
                        GameConstant.bankerTypeNameMap.get(bankerType) + "没有庄家，不能下注");
            }
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

    @Synchronized
    public void unBet(Player player, BetTypeEnum type) {
        if (!state.equals(IssueStateEnum.BETTING)) {
            throw ExceptionFactory.bizException("S_ISSUE_OUT_BET_TIME", "下注时间已过");
        }

        Integer money = player.unBet(type);
        updateTop(type, -money);
    }

    /**
     * 检查押注额度是否合理
     * @param betType 押注类型
     * @param money 押注金额
     */
    private boolean checkTop(BetTypeEnum betType, Integer money) {
        return topBetMap.get(betType).compareTo(money) > 0;
    }


    /**
     * 更新最大下注额
     * @param betType 下注类型
     * @param money 下注金额
     */
    private void updateTop(BetTypeEnum betType, Integer money) {
        betMap.put(betType, betMap.get(betType) + money);
        topBetMap = getTop(betMap);
    }

    private Map<BetTypeEnum, Integer> getTop(Map<BetTypeEnum, Integer> betMap) {
        Map<BetTypeEnum, Integer> topBetMap = new HashMap<>();
        betMap.forEach((betType, amount) -> {
            BankerTypeEnum bankerType = getBankTypeFromBetType(betType);
            List<BetTypeEnum> relatedBetTypes = GameConstant.bankerBetTypeMap.get(bankerType);
            relatedBetTypes.forEach(topBetType -> {
                Integer otherTotalBet = relatedBetTypes.stream()
                        .filter(relatedBetType -> !relatedBetType.equals(topBetType))
                        .reduce(0, (sum, otherBetType) -> sum + betMap.get(otherBetType), Integer::sum);
                Banker banker = bankerMap.get(bankerType);
                if (banker == null) {
                    return;
                }
                // 最大可下注额 = (其他区域下注总额 + 庄家身上的钱)/(赔率-1) - 已下注额
                Integer maxBet = BigDecimal.valueOf(otherTotalBet + banker.getMoney())
                        .divide(
                                GameConstant.betTypeMap.get(betType).getWinOdds().subtract(BigDecimal.ONE),
                                RoundingMode.FLOOR
                        ).intValue()
                        - betMap.get(topBetType);
                topBetMap.put(topBetType, maxBet);
            });
        });
        return topBetMap;
    }

    /**
     * 更新最高下注额，在庄家上庄时调用
     * @param bankerType 庄家类型
     * @param banker 庄家
     */
    private void updateTop(BankerTypeEnum bankerType, PointGameBanker banker) {
        List<BetTypeEnum> betTypes = GameConstant.bankerBetTypeMap.get(bankerType);
        betTypes.forEach(betType -> {
            topBetMap.put(
                    betType,
                    BigDecimal.valueOf(banker.getMoney())
                            .divide(
                                    GameConstant.betTypeMap.get(betType).getWinOdds().subtract(BigDecimal.ONE),
                                    BigDecimal.ROUND_FLOOR)
                            .intValue()
            );
        });
    }



    /**
     * 通过押注类型获得庄家类型
     * @param betType 押注类型
     * @return 庄家类型
     */
    private BankerTypeEnum getBankTypeFromBetType(BetTypeEnum betType) {
        switch (betType) {
            case BIG:
            case SMALL:
                return BankerTypeEnum.BIG_SMALL;
            case OOD:
            case EVEN:
                return BankerTypeEnum.OOD_EVEN;
            case NUMBER_0:
            case NUMBER_1:
            case NUMBER_2:
            case NUMBER_3:
            case NUMBER_4:
            case NUMBER_5:
            case NUMBER_6:
            case NUMBER_7:
            case NUMBER_8:
            case NUMBER_9:
                return BankerTypeEnum.NUMBER;
            default:
                assert false;
                return null;
        }
    }
}
