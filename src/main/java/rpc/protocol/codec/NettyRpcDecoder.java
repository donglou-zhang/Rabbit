package rpc.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import rpc.protocol.model.RpcMessage;

import java.util.List;

/**
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
        List<RpcMessage> messages = new DefaultRpcDecoder().decode(byteBuf.array());
        list.addAll(messages);
    }
}
