package com.zm.netty.quictstarttcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Describle This Class Is 客户端
 * @Author ZengMin
 * @Date 2020/7/18 11:02
 */
public class NettyClient {

    public static void main(String[] args) throws InterruptedException {
        // 创建时间循环组 实际上为一个线程池
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();

        // 创建服务端核心对象   服务器端是ServerBootStrap
        Bootstrap bootstrap = new Bootstrap();

        try {
            bootstrap.group(eventExecutors) // 设置线程组
                    .channel(NioSocketChannel.class)    // 设置客户端通道的默认实现类
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyClientHandler());     // 添加默认处理器
                        }
                    });
            // 连接服务器 异步
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6666).sync();
            System.out.println("client ok ..");
            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            // 优雅关闭
            eventExecutors.shutdownGracefully();
        }

    }


}
