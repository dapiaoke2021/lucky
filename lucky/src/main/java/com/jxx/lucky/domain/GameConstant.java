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

    public static Map<BankerTypeEnum, String> bankerTypeNameMap = new HashMap<BankerTypeEnum, String>() {{
        put(BankerTypeEnum.NUMBER, "扫雷");
        put(BankerTypeEnum.BIG_SMALL, "大小");
        put(BankerTypeEnum.OOD_EVEN, "单双");
        put(BankerTypeEnum.NN, "牛牛");
        put(BankerTypeEnum.SN, "扫牛");
    }};
}
