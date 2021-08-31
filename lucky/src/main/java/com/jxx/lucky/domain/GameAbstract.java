package com.jxx.lucky.domain;

import com.alibaba.cola.exception.ExceptionFactory;
import com.jxx.lucky.param.BetParam;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public abstract class GameAbstract implements Game {
    /**
     * 庄家
     */
    protected Banker banker;

    /**
     * 押注类型与最大可投注额，每次下注和撤销下注都会更新
     */
    protected Map<BetTypeEnum, Integer> topBetMap;

    /**
     * 押注类型与投注总额
     */
    protected Map<BetTypeEnum, Integer> betMap;

    protected String name;

    protected BankerTypeEnum bankerType;

    protected List<BetType> betTypes;

    /**
     * 参与玩家
     */
    protected Map<Long, Player> playerMap;

    public GameAbstract() {
        this.playerMap = new HashMap<>();
        this.topBetMap = new HashMap<>();

        this.betMap = new HashMap<>();
    }

    public void becomeBanker(Player player) {
        if (banker != null) {
            throw ExceptionFactory.bizException("S_ISSUE_ALREADY_HAS_BANKER","已有玩家坐庄");
        }

        this.banker = new Banker(player, bankerType);
        log.debug("游戏{} 庄家{}", this.name, this.banker);
        updateTop(banker);
    }


    @Override
    public void bet(Player player, List<BetParam> bets, String betNo){
        HashMap<BetTypeEnum, Integer> betMapTmp = new HashMap<>(betMap);
        bets.forEach(betParam -> {
            betMapTmp.merge(betParam.getBetType(), betParam.getAmount(), Integer::sum);
        });

        playerMap.putIfAbsent(player.getId(), player);
        bets.forEach(betParam -> {
            player.bet(betParam.getAmount(), betParam.getBetType(), betNo);
        });
        Map<BetTypeEnum, Integer> topBet = getTop(betMapTmp);
        this.betMap = betMapTmp;
        this.topBetMap = topBet;
    }

    @Override
    public void checkBet(Player player, List<BetParam> bets) {
        // 添加下注区域的赔率
        bets = bets.stream().map(this::setOdds).collect(Collectors.toList());

        // 检查玩家金额
        if(!player.check(bets)) {
            throw ExceptionFactory.bizException("S_PLAYER_NOT_ENOUGH_GOLD", "金额不足");
        }

        // 检查庄家是否存在
        if (banker == null) {
            throw ExceptionFactory.bizException("S_ISSUE_NO_BANKER", name + "没有庄家，不能下注");
        }

        // 检查庄家额度
        HashMap<BetTypeEnum, Integer> betMapTmp = new HashMap<>(betMap);
        bets.forEach(betParam -> {
            betMapTmp.merge(betParam.getBetType(), betParam.getAmount(), Integer::sum);
        });
        Map<BetTypeEnum, Integer> topBet = getTop(betMapTmp);
        for (BetParam betParam : bets) {
            if (topBet.get(betParam.getBetType()) < 0) {
                throw ExceptionFactory.bizException("S_ISSUE_NOT_ENOUGH_TOP_BET", name + "庄家额度不足");
            }
        }
    }

    public void open(String[] points) {
        if(banker == null) {
            return;
        }

        List<BetResult> betResults = getBetResults(points);
        banker.open(betMap, betResults);
        playerMap.forEach((id, player) -> player.open(betResults));
    }

    protected Map<BetTypeEnum, Integer> getTop(HashMap<BetTypeEnum, Integer> betMap) {
        Map<BetTypeEnum, Integer> topBetMap = new HashMap<>();
        List<BetType> relatedBetTypes = betTypes;
        relatedBetTypes.forEach(topBetType -> {
            Integer otherTotalBet = relatedBetTypes.stream()
                    .filter(relatedBetType -> !relatedBetType.equals(topBetType))
                    .reduce(0, (sum, otherBetType) -> sum + betMap.getOrDefault(otherBetType.getType(), 0), Integer::sum);
            // 最大可下注额 = (其他区域下注总额 + 庄家身上的钱)/(赔率-1) - 已下注额
            Integer maxBet = BigDecimal.valueOf(otherTotalBet + banker.getMoney())
                    .divide(
                            BigDecimal.valueOf(topBetType.getOdds() - 1),
                            RoundingMode.FLOOR
                    ).intValue()
                    - betMap.getOrDefault(topBetType.getType(), 0);
            topBetMap.put(topBetType.getType(), maxBet);
        });
        return topBetMap;
    }

    @Override
    public Banker getBanker() {
        return banker;
    }

    @Override
    public List<Player> getPlayers() {
        return new ArrayList<>(playerMap.values());
    }

    @Override
    public Map<BetTypeEnum, Integer> getTopBetMap() {
        return topBetMap;
    }

    @Override
    public Map<BetTypeEnum, Integer> getBetMap() {
        return betMap;
    }

    @Override
    public boolean containBetType(BetTypeEnum betTypeEnum) {
        return this.betTypes.stream().anyMatch(betType -> betType.getType().equals(betTypeEnum));
    }



    protected void updateTop(Banker banker) {
        betTypes.forEach(betType -> {
            topBetMap.put(betType.getType(), banker.getMoney()/ (betType.getOdds() - 1));
        });
    }
    public abstract List<BetResult> getBetResults(String[] points);
    protected abstract BetParam setOdds(BetParam betParam);
}
