package com.rpc2.zl.rpc.codec;

import com.rpc2.zl.rpc.protocol.RpcMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Created by Vincent on 2018/7/16.
 */
public class RpcDecoder extends LengthFieldBasedFrameDecoder{

    private static final int HEADER_SIZE = 4;

    private static final int LENGTH_FIELD_OFFSET = 0;

    private static final int LENGTH_FIELD_LENGTH = 4;

    private static final int MAX_FRAME_LENGTH = Integer.MAX_VALUE;

    private Class<?> genericClass;

    public RpcDecoder(Class<?> genericClass) {
        super(MAX_FRAME_LENGTH, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH);
        this.genericClass = genericClass;
    }

    public Object decode(ChannelHandlerContext ctx, ByteBuf in) {
        // TODO
        RpcMessage message = null;
        return message;
    }
}
