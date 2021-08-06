package com.jxx.user.component;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface UserConsumerInput {

    @Input("user-input")
    SubscribableChannel userInput();

}
