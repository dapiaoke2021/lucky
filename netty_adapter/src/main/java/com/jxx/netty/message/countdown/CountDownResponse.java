package com.jxx.netty.message.countdown;

import com.jxx.netty.dispatcher.Message;
import lombok.Data;

/**
 * 消息 - 倒计时
 */
@Data
public class CountDownResponse implements Message {

    /**
     * 类型 - 倒计时
     */
    public static final String TYPE = "COUNTDOWN_RESPONSE";

    private int countDown;

    @Override
    public String toString() {
        return "CountDownResponse{}";
    }

}
