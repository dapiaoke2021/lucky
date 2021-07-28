package com.jxx.lucky.domain;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author a1
 */
public class GameConstant {
    /**
     * 税收（只收取赢的部分）
     */
    public static BigDecimal TAX = BigDecimal.valueOf(0.05);

    public static Map<BetTypeEnum, BetType> betTypeMap = new HashMap<BetTypeEnum, BetType>(){{
        put(BetTypeEnum.BIG, new BetType(BetTypeEnum.BIG, point -> point.compareTo(4) > 0, BigDecimal.valueOf(2)));
        put(BetTypeEnum.SMALL, new BetType(BetTypeEnum.SMALL, point -> point.compareTo(4) <= 0, BigDecimal.valueOf(2)));
        put(BetTypeEnum.OOD, new BetType(BetTypeEnum.OOD, point -> point % 2 != 0, BigDecimal.valueOf(2)));
        put(BetTypeEnum.EVEN, new BetType(BetTypeEnum.EVEN, point -> point % 2 == 0, BigDecimal.valueOf(2)));
        put(BetTypeEnum.NUMBER_0, new BetType(BetTypeEnum.NUMBER_0, point -> point == 0, BigDecimal.valueOf(10)));
        put(BetTypeEnum.NUMBER_1, new BetType(BetTypeEnum.NUMBER_1, point -> point == 1, BigDecimal.valueOf(10)));
        put(BetTypeEnum.NUMBER_2, new BetType(BetTypeEnum.NUMBER_2, point -> point == 2, BigDecimal.valueOf(10)));
        put(BetTypeEnum.NUMBER_3, new BetType(BetTypeEnum.NUMBER_3, point -> point == 3, BigDecimal.valueOf(10)));
        put(BetTypeEnum.NUMBER_4, new BetType(BetTypeEnum.NUMBER_4, point -> point == 4, BigDecimal.valueOf(10)));
        put(BetTypeEnum.NUMBER_5, new BetType(BetTypeEnum.NUMBER_5, point -> point == 5, BigDecimal.valueOf(10)));
        put(BetTypeEnum.NUMBER_6, new BetType(BetTypeEnum.NUMBER_6, point -> point == 6, BigDecimal.valueOf(10)));
        put(BetTypeEnum.NUMBER_7, new BetType(BetTypeEnum.NUMBER_7, point -> point == 7, BigDecimal.valueOf(10)));
        put(BetTypeEnum.NUMBER_8, new BetType(BetTypeEnum.NUMBER_8, point -> point == 8, BigDecimal.valueOf(10)));
        put(BetTypeEnum.NUMBER_9, new BetType(BetTypeEnum.NUMBER_9, point -> point == 9, BigDecimal.valueOf(10)));
    }};

    public static Map<BankerTypeEnum, List<BetTypeEnum>> bankerBetTypeMap = new HashMap<BankerTypeEnum, List<BetTypeEnum>>(){{
        put(BankerTypeEnum.BIG_SMALL, Arrays.asList(BetTypeEnum.BIG, BetTypeEnum.SMALL));
        put(BankerTypeEnum.OOD_EVEN, Arrays.asList(BetTypeEnum.EVEN, BetTypeEnum.OOD));
        put(BankerTypeEnum.NUMBER, Arrays.asList(
                BetTypeEnum.NUMBER_0, BetTypeEnum.NUMBER_1, BetTypeEnum.NUMBER_2,
                BetTypeEnum.NUMBER_3, BetTypeEnum.NUMBER_4, BetTypeEnum.NUMBER_5,
                BetTypeEnum.NUMBER_6, BetTypeEnum.NUMBER_7, BetTypeEnum.NUMBER_8,
                BetTypeEnum.NUMBER_9
        ));
    }};
}
