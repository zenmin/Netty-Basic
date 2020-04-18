package com.zm.nio;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Describle This Class Is
 * @Author ZengMin
 * @Date 2020/4/16 21:24
 */
public class Channel01 {

    /**
     * 把数据通过fileChannel写入到文件中
     *
     * @throws Exception
     */
    @Test
    public void test1() throws Exception {
        String str = "测试";

        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\74170\\Desktop\\test.txt");
        // 通过fileOutputStream获取他的channel
        // 真实类型 FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();

        // 创建一个缓冲区 1024大小
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 数据写入缓冲区
        byteBuffer.put(str.getBytes());

        // 翻转buffer 将buffer变为读
        byteBuffer.flip();

        // 从buffer读取数据写到channel
        fileChannel.write(byteBuffer);

        // 关流
        fileOutputStream.close();

    }

    /**
     * 从文件中通过fileChannel读取信息到本地
     *
     * @throws Exception
     */
    @Test
    public void test2() throws Exception {
        File file = new File("C:\\Users\\74170\\Desktop\\test.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        // 获取输入流channel
        FileChannel channel = fileInputStream.getChannel();

        // 创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        //将channel中的数据读进buffer
        channel.read(byteBuffer);

        // 从buffer中读取数据
        System.out.println(new String(byteBuffer.array()));

        // 关流
        fileInputStream.close();

    }

    /**
     * 使用同一个buffer 拷贝文件
     *
     * @throws Exception
     */
    @Test
    public void test3() throws Exception {
        File file = new File("C:\\Users\\74170\\Desktop\\test.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        // 获取输入流channel
        FileChannel channel = fileInputStream.getChannel();

        // 创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        //将channel中的数据读进buffer
        channel.read(byteBuffer);

        // 从buffer中读取数据
        System.out.println(new String(byteBuffer.array()));


        // 创建输出流
        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\74170\\Desktop\\test2.txt");

        // 获取channel
        FileChannel outputStreamChannel = fileOutputStream.getChannel();

        // 翻转inputChannel的buffer
        byteBuffer.flip();

        // outputStreamChannel写入buffer
        outputStreamChannel.write(byteBuffer);

        // 关流
        fileInputStream.close();
        fileOutputStream.close();

    }


    /**
     * 使用transfer拷贝文件
     *
     * @throws Exception
     */
    @Test
    public void test4() throws Exception {
        File file = new File("C:\\Users\\74170\\Desktop\\test.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        // 获取输入流channel
        FileChannel inputStreamChannel = fileInputStream.getChannel();
        // 创建输出流
        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\74170\\Desktop\\test2.txt");

        // 获取输出流channel
        FileChannel outputStreamChannel = fileOutputStream.getChannel();

        // 从inputChannel拷贝文件
//        outputStreamChannel.transferFrom(inputStreamChannel, 0, inputStreamChannel.size());

        // 拷贝文件到目标channel
        inputStreamChannel.transferTo(0, inputStreamChannel.size(), outputStreamChannel);

        // 关流
        fileInputStream.close();
        fileOutputStream.close();

    }


}
