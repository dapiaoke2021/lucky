package com.jxx.lucky.param;


import com.jxx.lucky.domain.BetTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BetParam {
    BetTypeEnum betType;
    Integer amount;
}
