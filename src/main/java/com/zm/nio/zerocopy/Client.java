package com.zm.nio.zerocopy;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @Describle This Class Is 零拷贝客户端
 * @Author ZengMin
 * @Date 2020/6/6 11:38
 */
public class Client {

    public static void main(String[] args) throws Exception {

        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.connect(new InetSocketAddress("127.0.0.1", 1000));

        long start = System.currentTimeMillis();

        FileInputStream fileInputStream = new FileInputStream(new File("F:\\Java\\框架学习\\Netty\\资料\\netty-api-4.1中文.rar"));

        FileChannel channel = fileInputStream.getChannel();


        // 零拷贝  文件系统直接到目标channel
        // linux下transferTo一次可以传输完成全部文件   Win下一次只能传输8M文件 需要循环切割文件传输
        /*
         *  This method is potentially much more efficient than a simple loop
         *  that reads from this channel and writes to the target channel.  Many
         *  operating systems can transfer bytes directly from the filesystem cache
         *  to the target channel without actually copying them.
         */
        long position = channel.transferTo(0, channel.size(), socketChannel);

        channel.close();

        long end = System.currentTimeMillis();

        socketChannel.close();  // 客户端不关闭 会导致服务器一直throw远程主机强迫关闭了一个现有链接

        System.out.println("源文件大小：" + (8345 * 1024) + "文件位置：" + position + " , 耗时：" + (end - start));

    }

}
