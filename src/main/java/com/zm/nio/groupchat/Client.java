package com.zm.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @Describle This Class Is
 * @Author ZengMin
 * @Date 2020/5/17 11:53
 */
public class Client {

    private static final String HOST = "127.0.0.1";

    private static final Integer PORT = 6666;

    private Selector selector;

    private SocketChannel socketChannel;

    private String uname;

    public Client() throws IOException {
        selector = Selector.open();
        // 连接服务器
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
        // 设置非阻塞
        socketChannel.configureBlocking(false);
        // 注册到selector上
        socketChannel.register(selector, SelectionKey.OP_READ);
        // 得到uname
        uname = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(" --- 连接服务器成功  ----  ");
    }


    /**
     * 发送消息
     *
     * @param msg
     * @throws IOException
     */
    private void send(String msg) throws IOException {
        try {
            socketChannel.write(ByteBuffer.wrap((uname + "说：" + msg).getBytes()));
            System.out.println("      --->     发送成功!");
        } catch (IOException e) {
            System.out.println("与服务器丢失连接！");
            System.exit(1);
        }

    }

    private void read() throws IOException {
        try {
            int select = selector.select();
            if (select > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    // 如果是可读的事件
                    if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer allocate = ByteBuffer.allocate(1024);
                        // 把数据读到buffer里面
                        channel.read(allocate);
                        // 显示消息
                        System.out.println(new String(allocate.array()));
                    }
                }
                iterator.remove();  // 使用完移除当前key  防止重复操作
            } else {
                // 没有可用的通道
            }
        } catch (IOException e) {
        }
    }

    public static void main(String[] args) throws IOException {

        Client client = new Client();

        // 单开线程读取消息
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        client.read();
                        Thread.currentThread().sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            client.send(scanner.nextLine());
        }
    }


}
