package com.jxx.lucky.domain.nn;

import com.jxx.lucky.domain.ResultType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NNGameResultType extends ResultType implements Comparable<NNGameResultType> {
    protected NiuEnum niu;
    Integer maxPoint;

    public NNGameResultType(String point) {
        this.niu = getNiu(point);
        this.maxPoint = getMaxPoint(point);
        this.odds = getOdds(this.niu);
    }

    private Integer getOdds(NiuEnum niuEnum) {
        switch (niuEnum) {
            case NIL:
            case NIU_1:
            case NIU_2:
            case NIU_3:
            case NIU_4:
            case NIU_5:
            case NIU_6:
                return 2;
            case NIU_7:
                return 3;
            case NIU_8:
                return 4;
            case NIU_9:
                return 5;
            case NIU_NIU:
                return 6;
            default:
                return null;
        }
    }

    protected NiuEnum getNiu(String points) {
        Integer[] cards = new Integer[5];
        cards[0] = Integer.valueOf(String.valueOf(points.charAt(points.length() - 1)));
        cards[1] = Integer.valueOf(String.valueOf(points.charAt(points.length() - 2)));
        cards[2] = Integer.valueOf(String.valueOf(points.charAt(points.length() - 4)));
        cards[3] = Integer.valueOf(String.valueOf(points.charAt(points.length() - 5)));
        cards[4] = Integer.valueOf(String.valueOf(points.charAt(points.length() - 6)));

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

    protected Integer getMaxPoint(String points) {
        int maxPoint = 0;
        maxPoint = Math.max(Integer.parseInt(String.valueOf(points.charAt(points.length() - 1))), maxPoint);
        maxPoint = Math.max(Integer.parseInt(String.valueOf(points.charAt(points.length() - 2))), maxPoint);
        maxPoint = Math.max(Integer.parseInt(String.valueOf(points.charAt(points.length() - 4))), maxPoint);
        maxPoint = Math.max(Integer.parseInt(String.valueOf(points.charAt(points.length() - 5))), maxPoint);
        maxPoint = Math.max(Integer.parseInt(String.valueOf(points.charAt(points.length() - 6))), maxPoint);
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
