package com.zm.nio.zerocopy;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @Describle This Class Is 零拷贝服务端
 * @Author ZengMin
 * @Date 2020/6/6 11:38
 */
public class Server {

    public static void main(String[] args) throws Exception {

        InetSocketAddress address = new InetSocketAddress(1000);

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.bind(address);

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();
            int read = 0;
            while (true) {
                try {
                    read = socketChannel.read(byteBuffer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                byteBuffer.rewind();    // 倒带  重置buffer
            }
        }


    }


}
