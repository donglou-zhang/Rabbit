package com.rpc2.zl.rpc.common.keepalive;

import com.rpc2.zl.common.constant.Constants;
import com.rpc2.zl.rpc.protocol.RpcMessage;
import com.rpc2.zl.rpc.protocol.RpcMessageHeader;
import com.rpc2.zl.rpc.protocol.RpcRequest;
import com.rpc2.zl.rpc.protocol.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * Created by Vincent on 2018/7/16.
 */
public abstract class AbstractHeartbeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if(!(msg instanceof RpcMessage)) {
            ctx.fireChannelRead(msg);
            return;
        }
        RpcMessage message = (RpcMessage)msg;

        if(null == message || null == message.getHeader()) {
            ctx.fireChannelRead(message);
            return;
        }

        int msgType = message.getHeader().getMsgType();
        if(msgType== Constants.MESSAGE_TYPE_HEARTBEAT_PONG) {
            //TODO logger
        } else if(msgType == Constants.MESSAGE_TYPE_HEARTBEAT_PING) {
            this.sendPong(ctx);
        }
    }

    protected void sendPing(ChannelHandlerContext ctx) {
        RpcMessage message = new RpcMessage();

        RpcRequest request = new RpcRequest();
        request.setRequestId("ping");

        RpcMessageHeader header = new RpcMessageHeader();
        header.setMsgLength(1);
        header.setMsgType(Constants.MESSAGE_TYPE_HEARTBEAT_PING);
        message.setHeader(header);
        message.setBody(request);

        ctx.writeAndFlush(message);
    }

    /**
     * 返回response
     * @param ctx
     */
    protected void sendPong(ChannelHandlerContext ctx) {
        RpcMessage message = new RpcMessage();
        String body = new Date().toString(); // 对于心跳ping，server端返回给client端的内容为日期信息

        RpcResponse response = new RpcResponse();
        response.setResult(body);

        RpcMessageHeader header = new RpcMessageHeader();
        header.setMsgLength(body.length());
        header.setMsgType(Constants.MESSAGE_TYPE_HEARTBEAT_PONG);
        message.setHeader(header);
        message.setBody(response);

        ctx.writeAndFlush(message);
    }
}
