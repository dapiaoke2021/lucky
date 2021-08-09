package com.jxx.lucky.domain.point;

import com.jxx.lucky.domain.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author a1
 */
@Data
public class PointGameBanker extends Banker {

    public PointGameBanker() {
        this.result = 0;
    }

    public int open(List<BetType> hitBets, Map<BetTypeEnum, Integer> betMap) {
        // 从betMap中选出庄家的投注区
        int totalLose = hitBets.stream()
                .filter(
                        betType -> GameConstant.bankerBetTypeMap.get(type).stream().anyMatch(
                                betTypeEnum -> betType.getType().equals(betTypeEnum)))
                .reduce(
                        0,
                        (sum, betType) -> sum + BigDecimal.valueOf(betMap.get(betType.getType()))
                                    .multiply(betType.getWinOdds().subtract(BigDecimal.ONE)).intValue(),
                        Integer::sum);

        int totalWin = GameConstant.bankerBetTypeMap.get(type).stream()
                .filter(betTypeEnum -> hitBets.stream().noneMatch(hitBet -> hitBet.getType().equals(betTypeEnum)))
                .reduce(0, (sum, betTypeEnum) -> sum + betMap.get(betTypeEnum), Integer::sum );

        int totalTax = BigDecimal.valueOf(totalWin).multiply(GameConstant.TAX).intValue();
        this.result += (totalWin - totalTax - totalLose);
        return totalTax;
    }

    @Override
    public int open(Map<BetTypeEnum, Integer> betMap, List<BetTypeResult> bankerBetTypeResults) {
        return 0;
    }
}
