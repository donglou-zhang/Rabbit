package com.rabbit.zl.rpc.protocol.codec;

import com.rabbit.zl.rpc.protocol.model.RpcMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * when use netty to transfer data, this class is in charge of decode.
 *
 * @author Vincent
 * Created  on 2017/11/13.
 */
public class NettyRpcDecoder extends ByteToMessageDecoder{

    private Class<?> genericClass;

    public NettyRpcDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("Decoder: "+byteBuf.readableBytes());
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        List<RpcMessage> messages = new DefaultRpcDecoder().decode(bytes);
        list.addAll(messages);
    }
}
