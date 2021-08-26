package com.jxx.lucky.component;

import com.jxx.lucky.domain.BankerTypeEnum;
import com.jxx.lucky.event.BecameBankerEvent;
import com.jxx.lucky.event.IssueOpenedEvent;
import com.jxx.lucky.event.OffedBankerEvent;
import com.jxx.lucky.service.RobotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LuckyConsumer {
    @Autowired
    RobotService robotService;

    @Async
    @EventListener
    public void handleIssueOpenedEvent(IssueOpenedEvent issueOpenedEvent) {
        robotService.handleIssueOpenEvent();
    }

    @Async
    @EventListener
    public void handleBecameBankerEvent(BecameBankerEvent becameBankerEvent) {
        log.debug("收到消息：{}", becameBankerEvent);
        robotService.handleBecameBankerEvent(
                BankerTypeEnum.valueOf(becameBankerEvent.getBankerType()), becameBankerEvent.getPlayerId());
    }

    @Async
    @EventListener
    public void handleOffedBankerEvent(OffedBankerEvent offedBankerEvent) {
        robotService.handleOffedBankerEvent(offedBankerEvent.getPlayerId());
    }
}
