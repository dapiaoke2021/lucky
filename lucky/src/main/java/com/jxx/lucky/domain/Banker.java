package com.jxx.lucky.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author a1
 */
@Data
public class Banker {
    private Long userId;
    private Integer topBet;
    private BankerTypeEnum type;
    private Integer result;

    public Banker() {
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
                        (sum, betType) -> sum
                                + BigDecimal.valueOf(betMap.get(betType.getType()))
                                .multiply(betType.getOdds().subtract(BigDecimal.ONE)).intValue(),
                        Integer::sum );

        int totalWin = GameConstant.bankerBetTypeMap.get(type).stream()
                .filter(betTypeEnum -> hitBets.stream().noneMatch(hitBet -> hitBet.getType().equals(betTypeEnum)))
                .reduce(0, (sum, betTypeEnum) -> sum + betMap.get(betTypeEnum), Integer::sum );

        int totalTax = BigDecimal.valueOf(totalWin).multiply(GameConstant.TAX).intValue();
        this.result += (totalWin - totalTax - totalLose);
        return totalTax;
    }

}
