package com.rpc2.zl.rpc.codec;

import com.rpc2.zl.rpc.exception.RpcException;
import com.rpc2.zl.rpc.protocol.RpcMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by Vincent on 2018/7/16.
 */
public class RpcEncoder extends MessageToByteEncoder<RpcMessage> {

    private Class<?> genericClass;

    public RpcEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcMessage message, ByteBuf out) throws Exception {
        if(null == message) {
            throw new RpcException("RpcMessage is null, can not encode");
        }
        if(this.genericClass.isInstance(message.getBody())) {
            // TODO 序列化
            byte[] data = null;
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
