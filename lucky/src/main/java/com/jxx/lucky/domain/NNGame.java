package com.jxx.lucky.domain;

import com.jxx.lucky.domain.nn.NNGameResultType;
import com.jxx.lucky.param.BetParam;

import java.util.*;

public class NNGame extends GameAbstract{

    public NNGame() {
        super();
        this.betMap.put(BetTypeEnum.BET_1, 0);
        this.betMap.put(BetTypeEnum.BET_2, 0);
        this.betTypes = Arrays.asList(
                new BetType(BetTypeEnum.BET_1, 6), new BetType(BetTypeEnum.BET_2, 6)
        );
        this.name = "牛牛";
    }

    @Override
    protected Map<BetTypeEnum, Integer> getTop(HashMap<BetTypeEnum, Integer> betMap) {
        int bankerMoney = banker.getMoney();
        topBetMap.put(
                BetTypeEnum.BET_1,
                bankerMoney/5 - (betMap.get(BetTypeEnum.BET_1) + betMap.get(BetTypeEnum.BET_2)));
        topBetMap.put(
                BetTypeEnum.BET_2,
                bankerMoney/5 - (betMap.get(BetTypeEnum.BET_1) + betMap.get(BetTypeEnum.BET_2)));
        return topBetMap;
    }

    @Override
    protected void updateTop(Banker banker) {
        topBetMap.put(BetTypeEnum.BET_1, banker.getMoney()/5);
        topBetMap.put(BetTypeEnum.BET_2, banker.getMoney()/5);
    }

    @Override
    protected List<BetResult> getBetResults(String[] points) {
        String bankerPoint = points[4];
        String xianPoint1 = points[2];
        String xianPoint2 = points[3];
        NNGameResultType bankerResult = new NNGameResultType(bankerPoint);
        NNGameResultType xianResult1 = new NNGameResultType(xianPoint1);
        NNGameResultType xianResult2 = new NNGameResultType(xianPoint2);

        boolean bankerWin1 = bankerResult.compareTo(xianResult1) >= 0;
        boolean bankerWin2 = bankerResult.compareTo(xianResult2) >= 0;
        return Arrays.asList(
                new BetResult(bankerWin1, bankerWin1 ? bankerResult.odds : xianResult1.odds, BetTypeEnum.BET_1),
                new BetResult(bankerWin2, bankerWin1 ? bankerResult.odds : xianResult2.odds, BetTypeEnum.BET_2)
        );
    }

    @Override
    protected BetParam setOdds(BetParam betParam) {
        betParam.setTopOdds(5);
        return betParam;
    }
}
