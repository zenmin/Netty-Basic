package com.zm.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @Describle This Class Is
 * @Author ZengMin
 * @Date 2020/8/12 20:06
 */
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 拿到管道
        ChannelPipeline pipeline = ch.pipeline();

        // 加入一个netty提供的httpserverCodec
        // HttpServerCodec是netty提供的一个http编码和解码的处理器
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
        // 添加我们自己的处理器
        pipeline.addLast("MyHandler", new NettyServerHandler());
    }
}
