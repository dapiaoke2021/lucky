package com.jxx.lucky.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class Banker {
    protected Long userId;
    protected Integer money;
    protected Integer result;
    protected BankerTypeEnum type;

    public Banker(Player player, BankerTypeEnum type) {
        this.userId = player.id;
        this.money = player.money;
        this.result = 0;
        this.type = type;
    }

    public int open(Map<BetTypeEnum, Integer> betMap, List<BetResult> bankerBetTypeResults) {
        int totalWin = 0;
        int totalLose = 0;

        for (BetResult bankerBetTypeResult : bankerBetTypeResults) {
            if(bankerBetTypeResult.isBankerWin()) {
                totalWin += (bankerBetTypeResult.getOdds() - 1) * betMap.getOrDefault(bankerBetTypeResult.getBetType(), 0);
            } else {
                totalLose += (bankerBetTypeResult.getOdds() - 1) * betMap.getOrDefault(bankerBetTypeResult.getBetType(), 0);
            }
        }

        int tax = BigDecimal.valueOf(totalWin).multiply(GameConstant.TAX).intValue();
        result = totalWin - totalLose - tax;
        this.money += result;
        return tax;
    }
}
