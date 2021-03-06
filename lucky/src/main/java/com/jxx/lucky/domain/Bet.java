package com.jxx.lucky.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author a1
 */
@AllArgsConstructor
@Data
public class Bet {
    private Integer money;
    private BetTypeEnum betType;
    private String betNo;

    private Integer result;
}
