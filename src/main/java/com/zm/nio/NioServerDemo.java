package com.zm.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @Describle This Class Is
 * @Author ZengMin
 * @Date 2020/5/7 21:12
 */
public class NioServerDemo {

    public static void main(String[] args) throws IOException {

        int count = 0;

        // 创建一个serverSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 绑定端口
        serverSocketChannel.socket().bind(new InetSocketAddress(9999));

        // 设置serverSocketChannel为非阻塞
        serverSocketChannel.configureBlocking(false);

        // 创建Selector
        Selector selector = Selector.open();

        // 注册ServerSocketChannel到selector上  关心事件为accept
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {

            int select = selector.select(1000);
            // 无客户端连接
            if (select == 0) {
                continue;
            }


            // 从selector获取selectorKeys
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            if (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                // 判断selection的事件
                // 连接事件
                if (selectionKey.isAcceptable()) {
                    // 该客户端生成一个socketChannel
                    SocketChannel channel = serverSocketChannel.accept();
                    count++;
                    System.out.println("客户端连接：" + count);
                    channel.configureBlocking(false);
                    // 把这个通道注册到selector 事件为read 并制定一个buffer
                    channel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                // 读事件
                if (selectionKey.isReadable()) {
                    // 拿到通道 SelectableChannel
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    // 拿到buffer 注册时放的什么  这里可以直接强转什么   channel.register(Selector sel, int ops, Object att)
                    ByteBuffer attachment = (ByteBuffer) selectionKey.attachment();
                    // 把通道的数据读到buffer里面
                    channel.read(attachment);
                    System.out.println("客户端读取数据：" + new String(attachment.array()));
                }

                // 移除当前这个SelectionKey  防止重复操作
                iterator.remove();
            }
        }


    }

}
