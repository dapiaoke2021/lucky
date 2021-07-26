package com.jxx.lucky.domain;

import cn.hutool.core.math.Money;
import com.alibaba.cola.exception.ExceptionFactory;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (this.money.compareTo(money) < 0) {
            throw ExceptionFactory.bizException("S_PLAYER_NOT_ENOUGH_GOLD", "金额不足");
        }
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

    public void open(List<BetType> hitBets) {
        bonus = hitBets.stream().reduce(
                0,
                (sum, hitBet) -> {
                    Integer betMoney = bets.get(hitBet.getType());
                    if (betMoney == null) {
                        return sum;
                    }

                    // 赢钱 = 下注额 + 下注额 *（赔率-1）* (1 - 税收)
                    // 最低下注额1元，所以取整没问题
                    Integer winMoney = betMoney
                            + BigDecimal.valueOf(betMoney).multiply(
                                    hitBet.getOdds().subtract(BigDecimal.ONE))
                            .multiply(BigDecimal.ONE.subtract(GameConstant.TAX))
                            .intValue();

                    return sum + winMoney;
                },
                Integer::sum
        );
    }
}
