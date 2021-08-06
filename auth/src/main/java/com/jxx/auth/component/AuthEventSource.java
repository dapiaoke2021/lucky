package com.jxx.auth.component;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface AuthEventSource {

    @Output("auth-output")
    MessageChannel authOutput();
}
