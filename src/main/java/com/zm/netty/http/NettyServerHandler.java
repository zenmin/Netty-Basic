package com.zm.netty.http;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @Describle This Class Is
 * @Author ZengMin
 * @Date 2020/8/12 20:06
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * @param ctx
     * @param msg 客户端和服务器相互通讯的数据被封装成httpObject
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        // 判断http类型
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            System.out.println(request.uri());
            if (request.uri().equals("/favicon.ico")) {
                System.out.println("favicon.ico 不处理");
                return;
            }

            System.out.println("msg:" + msg.getClass());
            System.out.println("客户端地址:" + ctx.channel().remoteAddress());
            System.out.println("pipline:" + ctx.channel().hashCode());
            // 回复信息给浏览器 http方式
            ByteBuf byteBuf = Unpooled.copiedBuffer("hello!", CharsetUtil.UTF_8);
            // 构建一个http响应 即 httpResponse    放入byteBuf
            FullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
            // 响应头
            defaultFullHttpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            defaultFullHttpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
            // 将构建好的Responses输出
            ctx.writeAndFlush(defaultFullHttpResponse);
        }
        System.out.println(msg.getClass());
    }
}
