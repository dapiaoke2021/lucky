package com.jxx.lucky.domain;

import com.alibaba.cola.exception.ExceptionFactory;
import com.jxx.lucky.param.BetParam;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author a1
 */
@Data
public class Player {
    Long id;
    Map<BetTypeEnum, Integer> bets;
    Integer money;
    Integer bonus;

    public Player() {
        bets = new HashMap<>();
    }

    public void bet(Integer money, BetTypeEnum type){
        this.money -= money;
        bets.merge(type, money, Integer::sum);
    }

    public Integer unBet(BetTypeEnum type) {
        Integer totalBet = bets.get(type);
        if (totalBet == null) {
            throw ExceptionFactory.bizException("S_PLAYER_HAS_NOT_BET", "没有下注，撤销失败");
        }

        return totalBet;
    }

    public int open(List<BetType> hitBets) {
        int totalWin = hitBets.stream().reduce(
                0,
                (sum, hitBet) -> {
                    Integer betMoney = bets.get(hitBet.getType());
                    if (betMoney == null) {
                        return sum;
                    }

                    // 赢钱 = 下注额 *（赔率-1）
                    // 最低下注额1元，所以取整没问题
                    return sum + BigDecimal.valueOf(betMoney).multiply(hitBet.getOdds().subtract(BigDecimal.ONE)).intValue();
                },
                Integer::sum
        );
        int totalTax = hitBets.stream().reduce(
                0,
                (sum, hitBet) -> {
                    Integer betMoney = bets.get(hitBet.getType());
                    if (betMoney == null) {
                        return sum;
                    }

                    BigDecimal winMoney = BigDecimal.valueOf(betMoney).multiply(hitBet.getOdds().subtract(BigDecimal.ONE));
                    return sum + winMoney.multiply(GameConstant.TAX).intValue();

                },
                Integer::sum
        );

        int totalWinBet = hitBets.stream().reduce(
                0,
                (sum, hitBet) -> sum + ((bets.get(hitBet.getType()) == null) ? 0 : bets.get(hitBet.getType())),
                Integer::sum);

        bonus = totalWin - totalTax + totalWinBet;
        return totalTax;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id.equals(player.id);
    }

    public boolean check(List<BetParam> bets) {
        return bets.stream().reduce(
                0,
                (sum, betParam) -> sum + betParam.getAmount(),
                Integer::sum
        ).compareTo(money) <= 0;
    }
}
