package com.jxx.lucky.domain;

import com.jxx.lucky.domain.nn.NNGameResultType;

import java.util.Arrays;
import java.util.List;

public class NNGame2 extends NNGame{
    @Override
    protected List<BetResult> getBetResults(String[] points) {
        String bankerPoint = points[4];
        String xianPoint1 = points[2];
        String xianPoint2 = points[3];
        NNGameResultType bankerResult = new NNGameResultType2(bankerPoint);
        NNGameResultType xianResult1 = new NNGameResultType2(xianPoint1);
        NNGameResultType xianResult2 = new NNGameResultType2(xianPoint2);

        boolean bankerWin1 = bankerResult.compareTo(xianResult1) >= 0;
        boolean bankerWin2 = bankerResult.compareTo(xianResult2) >= 0;
        return Arrays.asList(
                new BetResult(bankerWin1, bankerWin1 ? bankerResult.odds : xianResult1.odds, BetTypeEnum.BET_1),
                new BetResult(bankerWin2, bankerWin1 ? bankerResult.odds : xianResult2.odds, BetTypeEnum.BET_2)
        );
    }
}
