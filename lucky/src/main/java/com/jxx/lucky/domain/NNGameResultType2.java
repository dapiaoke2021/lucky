package com.jxx.lucky.domain;

import com.jxx.lucky.domain.nn.NNGameResultType;
import com.jxx.lucky.domain.nn.NiuEnum;

public class NNGameResultType2 extends NNGameResultType {
    protected NiuEnum niu;
    Integer maxPoint;

    public NNGameResultType2(String point) {
        super(point);
    }

    @Override
    protected NiuEnum getNiu(String points){
        Integer[] cards = new Integer[5];
        cards[0] = Integer.valueOf(String.valueOf(points.charAt(points.length() - 1)));
        cards[1] = Integer.valueOf(String.valueOf(points.charAt(points.length() - 2)));

        int total = cards[0] + cards[1];
        if (total % 10 == 0) {
            return NiuEnum.NIU_NIU;
        }

        if (total > 10) {
            return NiuEnum.values()[total % 10];
        }

        return NiuEnum.NIL;
    }
}
