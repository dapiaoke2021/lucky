package com.jxx.lucky.domain;

import com.jxx.lucky.param.BetParam;

import java.util.List;
import java.util.Map;

public interface Game {
    void becomeBanker(Player player);
    void checkBet(Player player, List<BetParam> bets);
    void bet(Player player, List<BetParam> bets, String betNo);
    void open(String[] points);
    Banker getBanker();
    List<Player> getPlayers();
    Map<BetTypeEnum, Integer> getTopBetMap();
    Map<BetTypeEnum, Integer> getBetMap();
    boolean containBetType(BetTypeEnum betType);
}
