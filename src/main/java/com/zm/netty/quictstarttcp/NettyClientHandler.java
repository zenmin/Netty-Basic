package com.zm.netty.quictstarttcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @Describle This Class Is 客户端处理逻辑
 * @Author ZengMin
 * @Date 2020/7/18 11:19
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 客户端与服务器连接的channel就绪时的逻辑
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 给服务器发送消息
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello server", CharsetUtil.UTF_8));
    }

    /**
     * 当通道有读取时间时 会触发
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("服务器返回：" + byteBuf.toString(CharsetUtil.UTF_8));
    }


    /**
     * 出现异常
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
