package com.jxx.netty.message.chat;

import com.jxx.netty.dispatcher.Message;
import lombok.Data;

/**
 * 消息 - 心跳请求
 */
@Data
public class ChatSendToAllRequest implements Message {

    /**
     * 类型 - 广播消息
     */
    public static final String TYPE = "CHAT_SEND_TO_ALL_REQUEST";

    /**
     * 消息编号
     */
    private String msgId;
    /**
     * 内容
     */
    private String content;




    @Override
    public String toString() {
        return "ChatSendToAllRequest{}";
    }

}
