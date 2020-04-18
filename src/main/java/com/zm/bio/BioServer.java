package com.zm.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Describle This Class Is BioServer 利用线程池实现
 * @Author ZengMin
 * @Date 2020/4/11 11:54
 */
public class BioServer {


    public static void main(String[] args) throws IOException {

        // 创建线程池 处理客户端逻辑
        ExecutorService pool = Executors.newCachedThreadPool();

        // 创建serverSocket
        ServerSocket serverSocket = new ServerSocket(1000);

        // 监听客户端连接
        while (true) {

            // 拿到一个客户端连接 新建一个线程
            Socket accept = serverSocket.accept();

            pool.execute(() -> {
                System.out.println("收到客户端连接：" + accept.getInetAddress() + ", 处理线程：" + Thread.currentThread().getName());
                handle(accept);
            });
        }
    }

    public static void handle(Socket socket) {
        try {
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            // 循环读取客户端放的数据
            while (true) {
                int read = inputStream.read(bytes);
                if (read != -1) {
                    System.out.println("客户端发送数据：" + new String(bytes, 0, read));
                } else {
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
