package com.jxx.auth.component;

import com.jxx.auth.event.CreatedAccountEvent;
import com.jxx.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class UserConsumer {

    @Autowired
    IUserService userService;

    @Async
    @EventListener
    public void handleCreatedAccountEvent(CreatedAccountEvent createdAccountEvent) {
        userService.createUser(createdAccountEvent.getAccountVO().getId());
    }
}
