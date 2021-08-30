package com.jxx.netty.server.handler;

import com.jxx.netty.dispatcher.MessageDispatcher;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class NettyServerHandlerInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * 心跳超时时间
     */
    private static final Integer READ_TIMEOUT_SECONDS = 3 * 11;

    @Autowired
    private MessageDispatcher messageDispatcher;
    @Autowired
    private NettyServerHandler nettyServerHandler;

//    @Override
//    protected void initChannel(Channel ch) {
//        // 获得 Channel 对应的 ChannelPipeline
//        ChannelPipeline channelPipeline = ch.pipeline();
//        // 添加一堆 NettyServerHandler 到 ChannelPipeline 中
//        channelPipeline
//                // 空闲检测
//                .addLast(new ReadTimeoutHandler(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS))
//                // 编码器
//                .addLast(new InvocationEncoder())
//                // 解码器
//                .addLast(new InvocationDecoder())
//                .addLast(new HttpServerCodec())
//                .addLast(new ChunkedWriteHandler())
//                .addLast(new HttpObjectAggregator(8192))
//                .addLast(new WebSocketServerProtocolHandler("/hello"))
//                // 消息分发器
//                .addLast(messageDispatcher)
//                // 服务端处理器
//                .addLast(nettyServerHandler)
//        ;
//    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // websocket 相关的配置
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new ReadTimeoutHandler(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS));
        //因为基于http协议，使用http的编码和解码器
        pipeline.addLast(new HttpServerCodec());
        //是以块方式写，添加ChunkedWriteHandler处理器
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HttpObjectAggregator(8192));
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        // 自定义handler，处理业务逻辑
        pipeline.addLast(messageDispatcher);
        pipeline.addLast(nettyServerHandler);
        ;
    }
}
