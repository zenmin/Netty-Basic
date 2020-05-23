package com.zm.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @Describle This Class Is
 * @Author ZengMin
 * @Date 2020/5/15 19:32
 */
public class GroupChatServer {

    private Selector selector; // 定义selector

    private ServerSocketChannel listenerChannel;  // 专门用来监听客户端的ServerSocketChannel

    private static final int PORT = 6666; // 端口

    public GroupChatServer() {
        try {
            // 得到选择器
            selector = Selector.open();
            // 得到ServerSocketChannel
            listenerChannel = ServerSocketChannel.open();
            // 绑定贷款
            listenerChannel.bind(new InetSocketAddress(PORT));
            // 设置非阻塞
            listenerChannel.configureBlocking(false);
            // 注册到selector上
            listenerChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("  ----  聊天服务器启动成功 ---- ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 监听客户端请求
     */
    public void listen() {
        try {
            while (true) {
                int count = selector.select();
                if (count <= 0) {
                    return; // 无客户端连接
                }
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();

                    // 连接事件
                    if (selectionKey.isAcceptable()) {
                        // 拿到当前socketChannel
                        SocketChannel accept = listenerChannel.accept();
                        // 设置非阻塞
                        accept.configureBlocking(false);
                        // 将该socketChannel注册selector上
                        accept.register(selector, SelectionKey.OP_READ);
                        System.out.println(accept.getRemoteAddress().toString().substring(1) + " 上线了");
                    }

                    // 通道发生读事件
                    if (selectionKey.isReadable()) {
                        this.readData(selectionKey);
                    }
                    // 删除当前key 防止重复操作
                    iterator.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readData(SelectionKey selectionKey) {
        SocketChannel socketChannel = null;
        try {
            // 获取channel
            socketChannel = (SocketChannel) selectionKey.channel();
            // 创建buffer
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            // 把通道内输入读入buffer
            int count = socketChannel.read(byteBuffer);
            // 大于0说明有数据
            if (count > 0) {
                // 把buffer里面的数据转为字符串
                String msg = new String(byteBuffer.array());
                System.out.println("客户端发送消息：" + msg);
                // 给其他客户端发送消息
                this.sendToOtherClient(msg, socketChannel);
            }
        } catch (IOException e) {
//            e.printStackTrace();
            try {
                System.out.println(socketChannel.getRemoteAddress().toString().substring(1) + " 离线了");
                // 移除key 取消注册
                selectionKey.cancel();
                // 关闭通道
                socketChannel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }


    /**
     * 服务器转发消息 排除发消息的本机
     *
     * @param msg
     * @param myChannel
     */
    private void sendToOtherClient(String msg, SocketChannel myChannel) throws IOException {
        System.out.println("服务器转发消息中...");
        // 遍历所有客户端
        Set<SelectionKey> keys = selector.keys();
        for (SelectionKey key : keys) {
            // 得到channel
            Channel channel = key.channel();
            if (channel instanceof SocketChannel && channel != myChannel) {
                SocketChannel otherChannel = (SocketChannel) channel;
                // 创建buffer
                ByteBuffer allocate = ByteBuffer.wrap(msg.getBytes());
                // 通道数据写到buffer里面
                otherChannel.write(allocate);
                System.out.println("服务器转发成功");
            }
        }
    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }

}


















