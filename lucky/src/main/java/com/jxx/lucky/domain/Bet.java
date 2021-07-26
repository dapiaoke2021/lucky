package com.jxx.lucky.domain;

import lombok.Data;

/**
 * @author a1
 */
@Data
public class Bet {
    private Integer money;
    private BetTypeEnum betType;
}
