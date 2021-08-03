package com.jxx.user.event.handler;

import com.jxx.lucky.event.BecameBankerEvent;
import com.jxx.lucky.event.BetEvent;
import com.jxx.lucky.event.IssueOpenedEvent;
import com.jxx.lucky.event.OffedBankerEvent;
import com.jxx.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LuckyEventHandler {
    @Autowired
    IUserService userService;

    public void handleBetEvent(BetEvent betEvent) {
        userService.changeMoney(betEvent.getPlayerId(), -betEvent.getBetAmount());
    }

    public void handleBecameBankerEvent(BecameBankerEvent becameBankerEvent) {
        userService.changeMoney(becameBankerEvent.getPlayerId(), -becameBankerEvent.getTopBet());
    }

    public void handleOffedBankerEvent(OffedBankerEvent offedBankerEvent) {
        userService.changeMoney(offedBankerEvent.getPlayerId(), offedBankerEvent.getResult());
    }

    public void handleIssueOpenedEvent(IssueOpenedEvent issueOpenedEvent) {
        // 处理下注结果
        Map<Long, Integer> bonusMap = issueOpenedEvent.getResult();
        bonusMap.forEach((playerId, bonus) -> {
            if (bonus.compareTo(0) > 0) {
                userService.changeMoney(playerId, bonus);
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
