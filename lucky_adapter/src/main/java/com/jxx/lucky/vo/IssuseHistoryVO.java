package com.jxx.lucky.vo;

import com.jxx.lucky.domain.BetTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class IssuseHistoryVO {
    private BetTypeEnum betType;
    private Boolean valid;
}
