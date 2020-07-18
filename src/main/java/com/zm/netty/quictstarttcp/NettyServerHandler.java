package com.zm.netty.quictstarttcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @Describle This Class Is 自定义handler  继承handlerAdapter
 * @Author ZengMin
 * @Date 2020/7/18 10:21
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    /**
     * 通道读写方法  可以去到客户端发送的数据
     *
     * @param ctx 上下文对象  有pipline channel
     * @param msg 客户端发送的数据
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // netty的ByteBuffer 性能更好
        ByteBuf byteBuf = (ByteBuf) msg;
        // 打印客户端数据
        System.out.println("客户端：IP:" + ctx.channel().remoteAddress() + "----" + byteBuf.toString(CharsetUtil.UTF_8));
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
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello!", CharsetUtil.UTF_8));
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
