package com.jxx.netty.messagehandler.heartbeat;

import com.jxx.netty.codec.Invocation;
import com.jxx.netty.dispatcher.MessageHandler;
import com.jxx.netty.message.heartbeat.HeartbeatRequest;
import com.jxx.netty.message.heartbeat.HeartbeatResponse;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HeartbeatRequestHandler implements MessageHandler<HeartbeatRequest> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(Channel channel, HeartbeatRequest message) {
        logger.info("[execute][收到连接({}) 的心跳请求]", channel.id());
        // 响应心跳
        HeartbeatResponse response = new HeartbeatResponse();
        channel.writeAndFlush(new Invocation(HeartbeatResponse.TYPE, response).response());
    }

    @Override
    public String getType() {
        return HeartbeatRequest.TYPE;
    }

}
