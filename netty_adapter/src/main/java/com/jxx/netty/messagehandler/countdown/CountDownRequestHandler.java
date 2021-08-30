package com.jxx.netty.messagehandler.countdown;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.jxx.netty.codec.Invocation;
import com.jxx.netty.dispatcher.MessageHandler;
import com.jxx.netty.message.countdown.CountDownRequest;
import com.jxx.netty.message.countdown.CountDownResponse;
import com.jxx.netty.message.heartbeat.HeartbeatRequest;
import com.jxx.netty.message.heartbeat.HeartbeatResponse;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CountDownRequestHandler implements MessageHandler<CountDownRequest> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(Channel channel, CountDownRequest message) {
        logger.info("[execute][收到连接({}) 的倒计时请求]", channel.id());


        DateTime date = DateUtil.date();
        int seconds = date.getSeconds();

        // 响应倒计时
        CountDownResponse response = new CountDownResponse();
        response.setCountDown(60 - seconds);
        channel.writeAndFlush(new Invocation(CountDownResponse.TYPE, response).response());
    }

    @Override
    public String getType() {
        return CountDownRequest.TYPE;
    }


}
