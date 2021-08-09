package com.jxx.lucky.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.function.Function;

/**
 * @author a1
 */
@Data
public class BetType {

    /**
     * 投注类型
     */
    private BetTypeEnum type;


    /**
     * 赔率
     */
    private BigDecimal winOdds;


}
