package com.jxx.lucky.component;

import com.jxx.auth.event.CreatedAccountEvent;
import com.jxx.lucky.domain.BankerTypeEnum;
import com.jxx.lucky.event.BecameBankerEvent;
import com.jxx.lucky.event.IssueOpenedEvent;
import com.jxx.lucky.event.OffedBankerEvent;
import com.jxx.lucky.service.RobotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LuckyConsumer {
    @Autowired
    RobotService robotService;

    @StreamListener(value = "lucky-input", condition = "headers['rocketmq_TAGS'] == 'IssueOpenedEvent'")
    public void onMessage(@Payload IssueOpenedEvent issueOpenedEvent) {
        log.debug("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), issueOpenedEvent);
        robotService.handleIssueOpenEvent();
    }

    @StreamListener(value = "lucky-input", condition = "headers['rocketmq_TAGS'] == 'BecameBankerEvent'")
    public void onMessage(@Payload BecameBankerEvent becameBankerEvent) {
        log.debug("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), becameBankerEvent);
        robotService.handleBecameBankerEvent(
                BankerTypeEnum.valueOf(becameBankerEvent.getBankerType()), becameBankerEvent.getPlayerId());
    }

    @StreamListener(value = "lucky-input", condition = "headers['rocketmq_TAGS'] == 'OffedBankerEvent'")
    public void onMessage(@Payload OffedBankerEvent offedBankerEvent) {
        log.debug("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), offedBankerEvent);
        robotService.handleOffedBankerEvent(offedBankerEvent.getPlayerId());
    }
}
