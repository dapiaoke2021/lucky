package com.jxx.lucky.domain;

import lombok.Data;

@Data
public class DuiziGameResultType extends ResultType{
    BetTypeEnum betType;

    public DuiziGameResultType(String point) {
        Integer[] cards = new Integer[2];
        cards[1] = Integer.parseInt(String.valueOf(point.charAt(1)));
        cards[0] = Integer.parseInt(String.valueOf(point.charAt(0)));

        if (!cards[0].equals(cards[1])) {
            betType = BetTypeEnum.NIL;
        } else if(cards[0].compareTo(1) >= 0 && cards[0].compareTo(5) <= 0) {
            betType = BetTypeEnum.DUI_1_5;
            odds = 20;
        } else if (cards[0].compareTo(6) >= 0 && cards[0].compareTo(9) <= 0) {
            betType = BetTypeEnum.DUI_6_9;
            odds = 20;
        } else {
            betType = BetTypeEnum.values()[cards[0] + 18];
            odds = 98;
        }
    }
}
