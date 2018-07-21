package com.rpc2.zl.remoting.transport.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * SimpleChannelInboundHandler是继承自ChannelInboundHandlerAdapter，且SimpleChannelInboundHandler支持入站的消息用泛型表示
 * Created by Vincent on 2018/7/18.
 */
public abstract class AbstractHandler<T> extends SimpleChannelInboundHandler<T>{
    protected abstract void channelRead0(ChannelHandlerContext ctx, T t);
}
