package com.jxx.lucky.component;

import com.jxx.lucky.event.BecameBankerEvent;
import com.jxx.lucky.event.BetEvent;
import com.jxx.lucky.event.IssueOpenedEvent;
import com.jxx.lucky.event.OffedBankerEvent;
import com.jxx.user.enums.MoneyChangeTypeEnum;
import com.jxx.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class UserConsumer {

    @Autowired
    IUserService userService;

    @EventListener
    public void handleBetEvent(BetEvent betEvent) {
        userService.changeMoney(betEvent.getPlayerId(), -betEvent.getBetAmount(), MoneyChangeTypeEnum.BET);
    }

    @Async
    @EventListener
    public void handleBecameBankerEvent(BecameBankerEvent becameBankerEvent) {
        userService.changeMoney(
                becameBankerEvent.getPlayerId(),
                -becameBankerEvent.getTopBet(),
                MoneyChangeTypeEnum.BECOME_BANKER);
    }

    @Async
    @EventListener
    public void handleOffedBankerEvent(OffedBankerEvent offedBankerEvent) {
        userService.changeMoney(
                offedBankerEvent.getPlayerId(),
                offedBankerEvent.getMoney(), MoneyChangeTypeEnum.OFF_BANKER);
    }

    @Async
    @EventListener
    public void handleIssueOpenedEvent(IssueOpenedEvent issueOpenedEvent) {
        // 处理下注结果
        Map<Long, Integer> bonusMap = issueOpenedEvent.getResult();
        bonusMap.forEach((playerId, bonus) -> {
            if (!bonus.equals(0)) {
                userService.changeMoney(playerId, bonus, MoneyChangeTypeEnum.GAME);
            }
        });

        // 处理税
        Map<Long, Integer> taxMap = issueOpenedEvent.getTax();
        taxMap.forEach((playerId, tax) -> {
            if(tax.compareTo(0) > 0) {
                userService.increaseSale(playerId, tax);
            }
        });
    }

}
