package com.jxx.lucky.domain;

import com.alibaba.cola.exception.ExceptionFactory;
import lombok.Data;
import lombok.Synchronized;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author a1
 */
@Data
public class Issue {
    /**
     * 当前期号
     */
    private String issueNo;

    /**
     * 开奖点数
     */
    private Integer point;

    /**
     * 参与玩家
     */
    private Map<Long, Player> playerMap;

    /**
     * 庄家
     */
    private Map<BankerTypeEnum, Banker> bankerMap;

    /**
     * 押注类型与最大可投注额，每次下注和撤销下注都会更新
     */
    private Map<BetTypeEnum, Integer> topBetMap;

    /**
     * 押注类型与投注总额
     */
    private Map<BetTypeEnum, Integer> betMap;

    /**
     * 投注期状态
     */
    private IssueStateEnum state;

    /**
     * 总税收（开奖之后计算）
     */
    private Integer tax;

    public Issue() {
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

        Banker banker = new Banker();
        banker.setUserId(player.getId());
        banker.setTopBet(player.getMoney());
        banker.setType(bankerType);
        bankerMap.put(bankerType, banker);

        updateTop(bankerType, banker);
    }

    private boolean isBanker(Player player) {
        return bankerMap.values().stream().anyMatch(banker -> banker.getUserId().equals(player.id));
    }

    public void open(Integer point) {
        List<BetType> hitBets = getHitBets(point);
        int playerTotalTax = playerMap.values().stream().reduce(
                0,
                (sum, player) -> sum + player.open(hitBets),
                Integer::sum
        );
        int bankerTotalTax = bankerMap.values().stream().reduce(
                0,
                (sum, banker) -> sum + banker.open(hitBets, betMap),
                Integer::sum
        );
        this.tax = playerTotalTax + bankerTotalTax;
    }

    private List<BetType> getHitBets(Integer point) {
        return GameConstant.betTypeMap.values().stream()
                .filter(hitPre -> hitPre.getPredictor().apply(point))
                .collect(Collectors.toList());
    }

    @Synchronized
    public void bet(Player player, Integer money, BetTypeEnum type) {
        if (!state.equals(IssueStateEnum.BETTING)) {
            throw ExceptionFactory.bizException("S_ISSUE_OUT_BET_TIME", "下注时间已过");
        }

        BankerTypeEnum bankerType = getBankTypeFromBetType(type);
        if (bankerMap.get(bankerType) == null) {
            throw ExceptionFactory.bizException("S_ISSUE_NO_BANKER", "没有庄家，不能下注");
        }

        if (!checkTop(type, money)) {
            throw ExceptionFactory.bizException("S_ISSUE_NOT_ENOUGH_TOP_BET", "庄家额度不足");
        }

        playerMap.putIfAbsent(player.getId(), player);
        player.bet(money, type);
        updateTop(type, money);
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

        BankerTypeEnum bankerType = getBankTypeFromBetType(betType);
        List<BetTypeEnum> relatedBetTypes = GameConstant.bankerBetTypeMap.get(bankerType);
        relatedBetTypes.forEach(topBetType -> {
            Integer otherTotalBet = relatedBetTypes.stream()
                    .filter(relatedBetType -> !relatedBetType.equals(topBetType))
                    .reduce(0, (sum, otherBetType) -> sum + betMap.get(otherBetType), Integer::sum);
            Banker banker = bankerMap.get(bankerType);
            // 最大可下注额 = (其他区域下注总额 + 庄家身上的钱)/(赔率-1) - 已下注额
            Integer maxBet = BigDecimal.valueOf(otherTotalBet + banker.getTopBet())
                    .divide(
                            GameConstant.betTypeMap.get(betType).getOdds().subtract(BigDecimal.ONE),
                            BigDecimal.ROUND_FLOOR
                    ).intValue()
                    - betMap.get(topBetType);
            topBetMap.put(topBetType, maxBet);
        });
    }

    /**
     * 更新最高下注额，在庄家上庄时调用
     * @param bankerType 庄家类型
     * @param banker 庄家
     */
    private void updateTop(BankerTypeEnum bankerType, Banker banker) {
        List<BetTypeEnum> betTypes = GameConstant.bankerBetTypeMap.get(bankerType);
        betTypes.forEach(betType -> {
            topBetMap.put(
                    betType,
                    BigDecimal.valueOf(banker.getTopBet())
                            .divide(
                                    GameConstant.betTypeMap.get(betType).getOdds().subtract(BigDecimal.ONE),
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
