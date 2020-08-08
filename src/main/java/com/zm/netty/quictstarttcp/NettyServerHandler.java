package com.zm.netty.quictstarttcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * @Describle This Class Is 自定义handler  继承handlerAdapter
 * @Author ZengMin
 * @Date 2020/7/18 10:21
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    /**
     * 通道读写方法  可以取到客户端发送的数据
     *
     * @param ctx 上下文对象  有pipline channel
     * @param msg 客户端发送的数据
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 提交任务
        // 如果这里很耗时的逻辑  需要放入eventloop线程池taskQueue里面
        ctx.channel().eventLoop().execute(() -> {   // 提交到channel对应的eventloop的taskQueue中
            ByteBuf byteBuf = (ByteBuf) msg;
            System.out.println("1客户端：IP:" + ctx.channel().remoteAddress() + "----" + byteBuf.toString(CharsetUtil.UTF_8) + "-----" + Thread.currentThread().getName());
        });

        // 后提交的排队执行
        ctx.channel().eventLoop().execute(() -> {   // 提交到channel对应的eventloop的taskQueue中
            ByteBuf byteBuf = (ByteBuf) msg;
            System.out.println("2客户端：IP:" + ctx.channel().remoteAddress() + "----" + byteBuf.toString(CharsetUtil.UTF_8) +  "-----" + Thread.currentThread().getName());
        });


        // 提交定时任务 是提交到 scheduleTaskQueue中
        ctx.channel().eventLoop().schedule(() -> {
            ByteBuf byteBuf = (ByteBuf) msg;
            System.out.println("3 schedule 客户端：IP:" + ctx.channel().remoteAddress() + "----" + byteBuf.toString(CharsetUtil.UTF_8) +  "-----" + Thread.currentThread().getName());
        }, 10, TimeUnit.SECONDS);

        // netty的ByteBuffer 性能更好
        ByteBuf byteBuf = (ByteBuf) msg;
        // 打印客户端数据
        System.out.println("客户端：IP:" + ctx.channel().remoteAddress() + "----" + byteBuf.toString(CharsetUtil.UTF_8) +  "-----" + Thread.currentThread().getName());
    }


    /**
     * 数据读取完成  将数据写入到缓冲区并刷新
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 向客户端回送响应数据
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello complete!", CharsetUtil.UTF_8));
    }


    /**
     * 异常处理
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 关闭通道
        ctx.close();
    }


}
