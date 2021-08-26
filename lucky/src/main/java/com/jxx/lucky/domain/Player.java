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
    Integer tax;

    public Player() {
        bets = new ArrayList<>();
        bonus = 0;
        tax = 0;
    }

    public void bet(Integer money, BetTypeEnum type, String betNo){
        this.money -= money;
        bets.add(new Bet(money, type, betNo, null));
    }

    public Integer unBet(BetTypeEnum type) {
        // dummy
        return 0;
    }

    public int open(List<BetResult> playerBetResults) {
        int totalWin = 0;
        int totalLose = 0;
        int totalBet = 0;
        for (Bet bet : bets) {
            Optional<BetResult> playerBetResultOptional = playerBetResults.stream()
                    .filter(playerBetResult -> playerBetResult.getBetType().equals(bet.getBetType()))
                    .findAny();
            if (playerBetResultOptional.isPresent()) {
                BetResult playerBetResult = playerBetResultOptional.get();
                if (playerBetResult.isBankerWin()) {
                    totalLose += bet.getMoney() * (playerBetResult.getOdds() - 1);
                } else {
                    totalWin += bet.getMoney() * (playerBetResult.getOdds() - 1);
                }
                totalBet += bet.getMoney();
            }
        }

        // 一个玩家会出现在多个游戏
        int tax = BigDecimal.valueOf(totalWin).multiply(GameConstant.TAX).intValue();
        bonus += totalWin + totalBet - totalLose - tax;
        this.tax += tax;
        return this.tax;
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
                (sum, betParam) -> sum + betParam.getAmount()*betParam.getTopOdds(),
                Integer::sum
        ).compareTo(money) <= 0;
    }
}
