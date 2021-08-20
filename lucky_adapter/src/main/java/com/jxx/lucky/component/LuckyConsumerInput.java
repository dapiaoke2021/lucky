package com.jxx.lucky.component;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface LuckyConsumerInput {
    @Input("lucky-input")
    SubscribableChannel luckyInput();
}
