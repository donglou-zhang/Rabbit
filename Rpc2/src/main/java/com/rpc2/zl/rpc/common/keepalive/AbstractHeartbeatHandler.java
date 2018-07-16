package com.rpc2.zl.rpc.common.keepalive;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by Vincent on 2018/7/16.
 */
public abstract class AbstractHeartbeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

    }
}
