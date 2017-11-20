package rpc.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import rpc.protocol.model.RpcMessage;

/**
 * @author Vincent
 * Created  on 2017/11/13.
 */
public class NettyRpcEncoder extends MessageToByteEncoder<Object>{

    private Class<?> genericClass;

    public NettyRpcEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object in, ByteBuf byteBuf) throws Exception {
        if(genericClass.isInstance(in)) {
            byte[] data = new DefaultRpcEncoder().encode((RpcMessage) in);
            byteBuf.writeBytes(data);
        }
    }
}
