package com.jxx.lucky.domain;

import com.jxx.lucky.param.BetParam;
import lombok.Data;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author a1
 */
@Data
public class Player {
    Long id;
    List<Bet> bets;
    Integer money;
    Integer bonus;

    public Player() {
        bets = new ArrayList<>();
    }

    public void bet(Integer money, BetTypeEnum type, String betNo){
        this.money -= money;
        bets.add(new Bet(money, type, betNo, null));
    }

    public Integer unBet(BetTypeEnum type) {
        // dummy
        return 0;
    }

    public int open(List<BetType> hitBets) {

        hitBets.forEach(betType -> {
            bets.forEach(bet -> {
                boolean hit = hitBets.
                if ()
            });
            bets.stream()
                    .forEach()
                    .filter(bet -> bet.getBetType().equals(betType.getType()))
                    .forEach(bet -> bet.setResult());
        });

        int totalWin = hitBets.stream().reduce(
                0,
                (sum, hitBet) -> {
                    Integer betMoney = bets. bets.get(hitBet.getType());
                    if (betMoney == null) {
                        return sum;
                    }


                    // 赢钱 = 下注额 *（赔率-1）
                    // 最低下注额1元，所以取整没问题
                    return sum + BigDecimal.valueOf(betMoney).multiply(hitBet.getWinOdds().subtract(BigDecimal.ONE)).intValue();
                },
                Integer::sum
        );
        int totalTax = hitBets.stream().reduce(
                0,
                (sum, hitBet) -> {
                    Integer betMoney =  bets.get(hitBet.getType());
                    if (betMoney == null) {
                        return sum;
                    }

                    BigDecimal winMoney = BigDecimal.valueOf(betMoney).multiply(hitBet.getWinOdds().subtract(BigDecimal.ONE));
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
