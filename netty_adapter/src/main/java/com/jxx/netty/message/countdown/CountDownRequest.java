package com.jxx.netty.message.countdown;

import com.jxx.netty.dispatcher.Message;

/**
 * 消息 - 倒计时
 */
public class CountDownRequest implements Message {

    /**
     * 类型 - 倒计时
     */
    public static final String TYPE = "COUNTDOWN_REQUEST";

    @Override
    public String toString() {
        return "CountDownRequest{}";
    }

}
