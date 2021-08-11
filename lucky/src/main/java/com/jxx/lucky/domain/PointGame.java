package com.jxx.lucky.domain;

import com.jxx.lucky.domain.nn.NiuEnum;
import com.jxx.lucky.param.BetParam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class PointGame extends GameAbstract{

    public PointGame() {
        super();
        this.name = "扫牛";
        this.betTypes = Arrays.asList(
                new BetType(BetTypeEnum.NIU_1, 10), new BetType(BetTypeEnum.NIU_2, 10),
                new BetType(BetTypeEnum.NIU_3, 10), new BetType(BetTypeEnum.NIU_4, 10),
                new BetType(BetTypeEnum.NIU_5, 10), new BetType(BetTypeEnum.NIU_6, 10),
                new BetType(BetTypeEnum.NIU_7, 10), new BetType(BetTypeEnum.NIU_8, 10),
                new BetType(BetTypeEnum.NIU_9, 10), new BetType(BetTypeEnum.NIU_NIU, 10)
        );
    }


    @Override
    protected List<BetResult> getBetResults(String[] points) {
        NNPointGameResultType nnPointGameResultType = new NNPointGameResultType(points[4]);
        return betTypes.stream().map(betType -> {
            BetTypeEnum betTypeEnum = betType.getType();
            // 无牛庄家通吃
            if (nnPointGameResultType.getNiu().equals(NiuEnum.NIL)) {
                return new BetResult(true, 1, betTypeEnum);
            }

            boolean bankerWin = betTypeEnum.ordinal() - 3 != nnPointGameResultType.getNiu().ordinal();
            return new BetResult(bankerWin, bankerWin ? 2 : betType.getOdds(), betTypeEnum);
        }).collect(Collectors.toList());
    }

    @Override
    protected BetParam setOdds(BetParam betParam) {
        betParam.setTopOdds(1);
        return betParam;
    }
}
