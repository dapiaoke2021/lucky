package com.jxx.lucky.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author a1
 */
@Data
public class Banker {
    private Long userId;
    private Integer topBet;
    private BankerTypeEnum type;
}
