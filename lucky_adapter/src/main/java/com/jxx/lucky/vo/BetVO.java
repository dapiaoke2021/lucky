package com.jxx.lucky.vo;

import com.jxx.lucky.domain.BetTypeEnum;
import lombok.Data;

@Data
public class BetVO {
    private BetTypeEnum betType;
    private Integer bet;
    private Integer top;
}
