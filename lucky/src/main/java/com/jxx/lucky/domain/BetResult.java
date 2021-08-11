package com.jxx.lucky.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BetResult {
    private boolean bankerWin;
    private int odds;
    private BetTypeEnum betType;
}
