package com.jxx.lucky.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.function.Function;

/**
 * @author a1
 */
@Data
public class BetType {

    public BetType(BetTypeEnum type, Function<Integer, Boolean> predictor, BigDecimal odds) {
        this.type = type;
        this.predictor = predictor;
        this.odds = odds;
    }

    /**
     * 投注类型
     */
    private BetTypeEnum type;

    /**
     * 判断函数
     */
    private Function<Integer, Boolean> predictor;

    /**
     * 赔率
     */
    private BigDecimal odds;
}
