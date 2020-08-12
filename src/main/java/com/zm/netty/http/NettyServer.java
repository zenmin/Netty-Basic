package com.zm.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Describle This Class Is
 * @Author ZengMin
 * @Date 2020/8/12 20:06
 */
public class NettyServer {

    public static void main(String[] args) {

        EventLoopGroup bossGroup = new NioEventLoopGroup();

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)    // 设置客户端通道的默认实现类
                    .childHandler(new NettyServerInitializer());

            ChannelFuture channelFuture = serverBootstrap.bind(8083).sync();
            System.out.println("server started ...");
            channelFuture.channel().closeFuture().sync();    // 对通道关闭时间进行监听
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

}
