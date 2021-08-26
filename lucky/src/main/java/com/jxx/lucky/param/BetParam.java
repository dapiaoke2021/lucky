package com.jxx.lucky.param;


import com.jxx.lucky.domain.BetTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BetParam {
    BetTypeEnum betType;
    Integer amount;
    Integer topOdds;

    public BetParam(BetTypeEnum betTypeEnum, int amount, Integer odds) {
        this.betType = betTypeEnum;
        this.amount = amount;
        this.topOdds = odds;
    }
}
