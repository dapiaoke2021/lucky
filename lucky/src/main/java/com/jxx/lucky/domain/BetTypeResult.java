package com.jxx.lucky.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BetTypeResult {
    private BetTypeEnum betType;
    private boolean win;
    private ResultType resultType;
}
