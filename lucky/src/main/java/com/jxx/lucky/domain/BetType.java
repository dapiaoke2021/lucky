package com.jxx.lucky.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;
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
    private Integer odds;

    public BetType(BetTypeEnum type, Integer odds) {
        this.type = type;
        this.odds = odds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BetType betType = (BetType) o;
        return type == betType.type;
    }
}
