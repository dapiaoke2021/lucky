package com.jxx.user.component;

import com.jxx.auth.vo.AccountVO;
import com.jxx.lucky.event.BecameBankerEvent;
import com.jxx.lucky.event.BetEvent;
import com.jxx.lucky.event.IssueOpenedEvent;
import com.jxx.lucky.event.OffedBankerEvent;
import com.jxx.user.event.handler.LuckyEventHandler;
import com.jxx.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserConsumer {
    @Autowired
    IUserService userService;

    @Autowired
    LuckyEventHandler luckyEventHandler;

    @StreamListener(value = "user-input", condition = "headers['rocketmq_TAGS'] == 'AccountVO'")
    public void onMessage(@Payload AccountVO accountVO) {
        log.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), accountVO);
        userService.createUser(accountVO.getId());
    }

    @StreamListener(value = "user-input", condition = "headers['rocketmq_TAGS'] == 'BetEvent'")
    public void onMessage(@Payload BetEvent betEvent) {
        log.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), betEvent);
        luckyEventHandler.handleBetEvent(betEvent);
    }

    @StreamListener(value = "user-input", condition = "headers['rocketmq_TAGS'] == 'BecameBankerEvent'")
    public void onMessage(@Payload BecameBankerEvent becameBankerEvent) {
        log.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), becameBankerEvent);
        luckyEventHandler.handleBecameBankerEvent(becameBankerEvent);
    }

    @StreamListener(value = "user-input", condition = "headers['TAGS'] == 'OffedBankerEvent'")
    public void onMessage(@Payload OffedBankerEvent offedBankerEvent) {
        log.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), offedBankerEvent);
        luckyEventHandler.handleOffedBankerEvent(offedBankerEvent);
    }

    @StreamListener(value = "user-input", condition = "headers['rocketmq_TAGS'] == 'IssueOpenedEvent'")
    public void onMessage(@Payload IssueOpenedEvent issueOpenedEvent) {
        log.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), issueOpenedEvent);
        luckyEventHandler.handleIssueOpenedEvent(issueOpenedEvent);
    }
}
