package com.jxx.lucky.domain.nn;

import com.jxx.lucky.domain.Banker;
import com.jxx.lucky.domain.BetTypeEnum;
import com.jxx.lucky.domain.BetTypeResult;
import com.jxx.lucky.domain.GameConstant;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class NNBanker extends Banker {

    @Override
    public int open(Map<BetTypeEnum, Integer> betMap, List<BetTypeResult> bankerBetTypeResults) {
        int totalWin = 0;
        int totalLose = 0;
        for (BetTypeResult betTypeResult : bankerBetTypeResults) {
            BetTypeEnum betType = betTypeResult.getBetType();
            if (betType.equals(BetTypeEnum.BET_1) || betType.equals(BetTypeEnum.BET_2)) {
                int totalBet = betMap.get(betType);
                if (betTypeResult.isWin()) {
                    totalWin += totalBet * (betTypeResult.getResultType().getOdds() - 1);
                } else {
                    totalLose += totalBet * (betTypeResult.getResultType().getOdds() - 1);
                }
            }
        }

        int tax = BigDecimal.valueOf(totalWin).multiply(GameConstant.TAX).intValue();
        result = totalWin - totalLose - tax;
        return tax;
    }
}
