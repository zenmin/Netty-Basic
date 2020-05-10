package com.zm.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.UUID;

/**
 * @Describle This Class Is
 * @Author ZengMin
 * @Date 2020/5/10 11:54
 */
public class NioClientDemo {

    public static void main(String[] args) throws IOException {

        // 创建socketChannel
        SocketChannel channel = SocketChannel.open();

        // 设置为非阻塞
        channel.configureBlocking(false);

        // 创建端口地址
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 9999);

        // 执行连接
        boolean connect = channel.connect(inetSocketAddress);

        // 服务端繁忙连接失败的情况
        if (!connect) {
            // 客户端不会阻塞 可以做其他工作
            while (!channel.finishConnect()) {
                System.out.println("服务器繁忙");
            }
        }

        // 连接成功 发送数据
        String str = "Hello NIO 阿" + UUID.randomUUID();
        // 包装一个数据  无需关系大小
        ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());

        // 写入数据
        channel.write(byteBuffer);
        // 阻塞客户端
        System.in.read();
    }

}
