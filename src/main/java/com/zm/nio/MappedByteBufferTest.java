package com.zm.nio;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Describle MappedByteBuffer 可以允许直接在内存中（堆外内存） 操作系统不需要拷贝一次
 * @Author ZengMin
 * @Date 2020/4/19 11:14
 */
public class MappedByteBufferTest {

    @Test
    public void test1() throws IOException {
        // 获取文件  读写模式
        RandomAccessFile randomAccessFile = new RandomAccessFile(new File("1.txt"), "rw");
        // 获取通道
        FileChannel channel = randomAccessFile.getChannel();

        // 创建MappedByteBuffer                           模式                 起始位置     读取多大的数据 此处5个字节  即下标0~4
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        map.put(0, (byte) 'A');
        map.put(4, (byte) 'A');
        System.out.println("修改完成");

        randomAccessFile.close();

    }


}
