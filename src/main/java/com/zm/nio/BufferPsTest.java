package com.zm.nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @Describle This Class Is Buffer的注意事项
 * @Author ZengMin
 * @Date 2020/4/19 10:58
 */
public class BufferPsTest {

    /**
     * 取数据时要和存的顺序一致
     */
    @Test
    public void test1() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 先放入不同类型的数据
        byteBuffer.putInt(1);
        byteBuffer.putChar('a');
        byteBuffer.putDouble(2.3);

        // 转为可读
        byteBuffer.flip();

        // 取出  必须按照顺序取
//        char aChar = byteBuffer.getChar();
//        System.out.println(aChar);
        int anInt = byteBuffer.getInt();
        System.out.println(anInt);
        char aChar = byteBuffer.getChar();
        System.out.println(aChar);
        double aDouble = byteBuffer.getDouble();
        System.out.println(aDouble);

    }


    /**
     * 只读buffer不可再写入
     */
    @Test
    public void test2() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 先放入不同类型的数据
        for (int i = 0; i < 10; i++) {
            byteBuffer.put((byte) i);
        }

        // 变为可读
        byteBuffer.flip();
        // 转为只读
        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();

        for (int i = 0; i < 10; i++) {
            System.out.println(readOnlyBuffer.get());
        }


        // 只读buffer不可再写入
//        readOnlyBuffer.flip();
//        readOnlyBuffer.put((byte) 10);

    }

    /**
     * Scattering  将数据写入到buffer时  可以采用buffer数组  将会依次写入 （分散）
     * Gathering   从buffer读取数据时  可以采用buffer数据 将会依次读出  聚集
     */
    @Test
    public void test3() throws IOException {
        // 采用socket方式
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 创建socket端口
        InetSocketAddress address = new InetSocketAddress(1000);

        // 绑定端口
        serverSocketChannel.bind(address);

        // 接收请求  telnet方式
        SocketChannel accept = serverSocketChannel.accept();

        // 创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(3);
        byteBuffers[1] = ByteBuffer.allocate(5);
        int acceptLength = 8;
        while (true) {
            int read = 0;
            // 如果小于8个字节就一直往bytebuffers里面读
            while (read < acceptLength) {
                long len = accept.read(byteBuffers);
                read += len;

                // 打印当前每个buffer的position和limit
                Arrays.asList(byteBuffers).stream().forEach(b -> {
                    System.out.println(" position:" + b.position() + ",limit:" + b.limit());
                });
            }

            // 翻转每个buffer准备读取
            Arrays.asList(byteBuffers).stream().forEach(b -> {
                b.flip();
            });

            // 回送给客户端
            int write = 0;
            while (write < acceptLength) {
                long w = accept.write(byteBuffers);
                write += w;
            }

            // 清空所有buffer
            Arrays.asList(byteBuffers).forEach(b -> {
                b.clear();
            });


        }


    }

}
















