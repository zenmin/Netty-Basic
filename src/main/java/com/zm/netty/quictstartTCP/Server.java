package com.zm.netty.quictstartTCP;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Describle This Class Is nettyServer
 * @Author ZengMin
 * @Date 2020/7/5 12:28
 */
public class Server {

    public static void main(String[] args) throws InterruptedException {
        // 创建BossGroup  和  WorkGroup
        /**
         * 创建两个线程组  boos和work
         * boos只处理连接请求  真正和客户端业务处理 只交给workGroup处理
         * 两个都是无限循环
         */
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

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
                                socketChannel.pipeline().addLast(null); // 拿到管道 添加一个处理器进去
                            }
                        }); // 给boosGroup  workGroup 设置默认处理器

        System.out.println("server config success");

        // 绑定端口
        ChannelFuture channelFuture = serverBootstrap.bind(6666).sync();

        // 对关闭通道进行监听
        channelFuture.channel().closeFuture().sync();
    }
}
