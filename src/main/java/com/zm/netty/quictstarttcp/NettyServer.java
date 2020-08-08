package com.zm.netty.quictstarttcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Describle This Class Is nettyServer
 * @Author ZengMin
 * @Date 2020/7/5 12:28
 */
public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        /*
         * 创建BossGroup  和  WorkGroup
         *
         * 创建两个线程组  boos和work
         * boos只处理连接请求  真正和客户端业务处理 只交给workGroup处理
         * 两个都是无限循环
         */
        // 默认线程数CPU核心数 * 2  DEFAULT_EVENT_LOOP_THREADS = Math.max(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));
        NioEventLoopGroup boosGroup = new NioEventLoopGroup(1); // 这里线程组中只要一个线程
        NioEventLoopGroup workGroup = new NioEventLoopGroup(8);

        try {

            // 创建服务启动核心对象
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 配置必要参数
            serverBootstrap.group(boosGroup, workGroup) // 设置两个group
                    .channel(NioServerSocketChannel.class)  // 设置NioServerSocketChannel作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 1024) // 设置服务器得到的线程个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)  // 设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {   // 创建一个通道测试对象
                        // 设置pipline设置处理器
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyServerHandler()); // 拿到管道 添加一个处理器进去
                        }
                    }); // 给boosGroup  workGroup 设置默认处理器

            System.out.println("server config success");

            // 绑定端口
            ChannelFuture channelFuture = serverBootstrap.bind(6666).sync();

            // 给channelFuture注册监听器  监听我们关系的事件
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("绑定端口成功！");
                    }
                    if (channelFuture.isCancelled()) {
                        System.out.println("绑定端口失败！");
                        System.out.println("异常信息：" + channelFuture.cause().toString());
                    }
                }
            });

            // 对关闭通道进行监听 异步
            channelFuture.channel().closeFuture().sync();
        } finally {
            boosGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
