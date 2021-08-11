package com.jxx.lucky.domain.nn;

import com.alibaba.cola.exception.ExceptionFactory;
import com.jxx.lucky.domain.*;
import com.jxx.lucky.param.BetParam;
import lombok.Data;
import lombok.Synchronized;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class IssueNN extends Issue {
    /**
     * 总税收（开奖之后计算）
     */
    private Integer tax;

    private Map<BankerTypeEnum, Game> gameMap;


    public IssueNN() {
        this.state = IssueStateEnum.BETTING;
        gameMap = new HashMap<>();
    }

    public void buildIssue(BankerTypeEnum bankerTypeEnum, Game game) {
        gameMap.put(bankerTypeEnum, game);
    }

    @Synchronized
    @Override
    public void becomeBanker(BankerTypeEnum bankerType, Player player) {
        Game game = gameMap.get(bankerType);
        if (game == null) {
            throw ExceptionFactory.bizException("S_ISSUE_BANKER_TYPE_ERROR","庄家类型错误");
        }

        game.becomeBanker(player);
    }

    public Banker getBanker(BankerTypeEnum bankerType) {
        return gameMap.get(bankerType).getBanker();
    }

    public Map<BetTypeEnum, Integer> getTopBetMap() {
        return gameMap.values().stream().map(Game::getTopBetMap)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void open(String[] points) {
        gameMap.forEach((bankerType, game) -> game.open(points));
    }

    @Override
    public void bet(Player player, List<BetParam> bets, String betNo) {
        if (!state.equals(IssueStateEnum.BETTING)) {
            throw ExceptionFactory.bizException("S_ISSUE_OUT_BET_TIME", "下注时间已过");
        }

        if(bets.stream().anyMatch(bet -> getBankTypeFromBetType(bet.getBetType()) == null)) {
            throw ExceptionFactory.bizException("S_ISSUE_NO_BANKER", "游戏未配置");
        }

        Map<BankerTypeEnum, List<BetParam>> gameBetsMap
                = bets.stream().collect(Collectors.groupingBy(bet -> getBankTypeFromBetType(bet.getBetType())));
        gameBetsMap.keySet().stream().anyMatch(bankerType -> gameMap.get(bankerType) == null);
        gameBetsMap.forEach((bankerType, gameBets) -> {
            gameMap.get(bankerType).checkBet(player, gameBets);
        } );
        gameBetsMap.forEach((bankerType, gameBets) -> {
            gameMap.get(bankerType).bet(player, gameBets, betNo);
        });
    }

    public Map<Long, Player> getPlayerMap() {
        return gameMap.values().stream().map(Game::getPlayers)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Player::getId, Function.identity()));
    }

    private BankerTypeEnum getBankTypeFromBetType(BetTypeEnum betType) {
         Optional<Map.Entry<BankerTypeEnum, Game>> gameEntry = gameMap.entrySet().stream()
                .filter(bankerTypeGameEntry -> bankerTypeGameEntry.getValue().containBetType(betType))
                .findAny();
         return gameEntry.map(Map.Entry::getKey).orElse(null);
    }
}
