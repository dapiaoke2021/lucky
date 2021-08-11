package com.jxx.lucky.domain;

import com.jxx.lucky.param.BetParam;

import java.util.*;
import java.util.stream.Collectors;

public class DuiziGame extends GameAbstract{

    public DuiziGame() {
        super();
        this.name = "对子";
        this.betTypes = Arrays.asList(
                new BetType(BetTypeEnum.DUI_1_5, 20), new BetType(BetTypeEnum.DUI, 10),
                new BetType(BetTypeEnum.DUI_6_9, 20), new BetType(BetTypeEnum.DUI00, 98),
                new BetType(BetTypeEnum.DUI11, 98), new BetType(BetTypeEnum.DUI22, 98),
                new BetType(BetTypeEnum.DUI33, 98), new BetType(BetTypeEnum.DUI44, 98),
                new BetType(BetTypeEnum.DUI55, 98), new BetType(BetTypeEnum.DUI66, 98),
                new BetType(BetTypeEnum.DUI77, 98), new BetType(BetTypeEnum.DUI88, 98),
                new BetType(BetTypeEnum.DUI99, 98)
        );
    }

    @Override
    protected List<BetResult> getBetResults(String[] points) {
        String point = points[4];
        Integer[] cards = new Integer[2];
        cards[1] = Integer.parseInt(String.valueOf(point.charAt(point.length() - 2)));
        cards[0] = Integer.parseInt(String.valueOf(point.charAt(point.length() - 1)));

        if (!cards[0].equals(cards[1])) {
            return betTypes.stream()
                    .map(betType -> new BetResult(true, 2, betType.getType()))
                    .collect(Collectors.toList());
        }

        return betTypes.stream().map(betType -> {
            switch (betType.getType()) {
                case DUI:
                    return new BetResult(false, betType.getOdds(), betType.getType());
                case DUI_1_5:
                    if (cards[0].compareTo(1) >= 0 && cards[0].compareTo(5) <= 0) {
                        return new BetResult(false, betType.getOdds(), betType.getType());
                    } else {
                        return new BetResult(true, 2, betType.getType());
                    }
                case DUI_6_9:
                    if (cards[0].compareTo(6) >= 0 && cards[0].compareTo(9) <= 0) {
                        return new BetResult(false, betType.getOdds(), betType.getType());
                    } else {
                        return new BetResult(true, 2, betType.getType());
                    }
                default:
                    if (betType.getType().equals(BetTypeEnum.values()[cards[0] + 18])) {
                        return new BetResult(false, betType.getOdds(), betType.getType());
                    } else {
                        return new BetResult(true, 2, betType.getType());
                    }
            }
        }).collect(Collectors.toList());
    }

    @Override
    protected BetParam setOdds(BetParam betParam) {
        betParam.setTopOdds(1);
        return betParam;
    }
}
