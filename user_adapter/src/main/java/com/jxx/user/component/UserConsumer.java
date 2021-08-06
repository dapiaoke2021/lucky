package com.jxx.user.component;

import com.jxx.auth.service.IAccountService;
import com.jxx.auth.vo.AccountVO;
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

    @StreamListener("user-input")
    public void onMessage(@Payload AccountVO accountVO) {
        log.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), accountVO);
        userService.createUser(accountVO.getId());
    }
}
