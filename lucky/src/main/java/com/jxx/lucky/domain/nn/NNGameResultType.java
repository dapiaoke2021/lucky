package com.jxx.lucky.domain.nn;

import com.jxx.lucky.domain.ResultType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NNGameResultType extends ResultType implements Comparable<NNGameResultType> {
    NiuEnum niu;
    Integer maxPoint;

    public NNGameResultType(String point) {
        this.niu = getNiu(point);
        this.maxPoint = getMaxPoint(point);
        this.odds = getOdds(this.niu);
    }

    private BigDecimal getOdds(NiuEnum niuEnum) {
        return BigDecimal.ONE;
    }

    private NiuEnum getNiu(String points) {
        Integer[] cards = new Integer[5];
        for (int i = 0; i < points.length(); i++) {
            cards[i] = Integer.valueOf(String.valueOf(points.charAt(i)));
        }

        // 计算总点数
        int sums = 0;
        for (int i = 0; i < cards.length; i++) {
            if (cards[i] > 10) {
                cards[i] = 10;
            }
            sums += cards[i];
        }
        //牛牛
        if (sums % 10 == 0) {
            return NiuEnum.NIU_NIU;
        }
        // 牛丁 ~ 牛九
        int bull = 0;
        for (int i = 0; i < cards.length - 2; i++) {
            int cardI = cards[i];
            for (int j = i + 1; j < cards.length; j++) {
                int cardJ = cards[j];
                for (int k = j + 1; k < cards.length; k++) {
                    int cardK = cards[k];
                    int total = cardI + cardJ + cardK;
                    if (total % 10 == 0) {
                        int n = (sums - total) % 10;
                        bull = Math.max(bull, n);
                    }
                }
            }
        }
        if (bull == 0) {
            return NiuEnum.NIL;
        } else {
            return NiuEnum.values()[bull];
        }
    }

    private Integer getMaxPoint(String points) {
        int maxPoint = 0;
        for (int i = 0; i < points.length(); i++) {
            maxPoint = Math.max(Integer.parseInt(String.valueOf(points.charAt(i))), maxPoint);
        }
        return maxPoint;
    }

    @Override
    public int compareTo(NNGameResultType o) {
        if (this.getNiu().compareTo(o.getNiu()) != 0) {
            return this.getNiu().compareTo(o.getNiu());
        }

        if (this.getMaxPoint().compareTo(o.getMaxPoint()) != 0) {
            return this.getMaxPoint().compareTo(o.getMaxPoint());
        }

        return 0;
    }


}
