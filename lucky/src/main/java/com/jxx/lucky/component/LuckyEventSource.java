package com.jxx.lucky.component;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface LuckyEventSource {

    @Output("lucky-output")
    MessageChannel luckyOutput();
}
