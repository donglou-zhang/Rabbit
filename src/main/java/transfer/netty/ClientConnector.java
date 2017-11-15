package transfer.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.protocol.codec.NettyRpcDecoder;
import rpc.protocol.codec.NettyRpcEncoder;
import rpc.protocol.model.RpcMessage;
import rpc.transmission.AbstractRpcConnector;

/**
 *
 * @author Vincent
 * Created  on 2017/11/13.
 */
public class ClientConnector extends AbstractRpcConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientConnector.class);

    private static final Object lock  = new Object();

    public RpcMessage send(RpcMessage request, boolean async) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        final ResultHandler resultHandler = new ResultHandler();
        try {
            Bootstrap bs = new Bootstrap();
            bs.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline cp = socketChannel.pipeline();
                            cp.addLast("RpcMessageEncoder", new NettyRpcEncoder());
                            cp.addLast("RpcMessageDecoder", new NettyRpcDecoder());
                            cp.addLast("ResultHandler", resultHandler);
                        }
                    });

            ChannelFuture future = bs.connect(getRemoteHost(), getRemotePort()).sync();
            future.channel().writeAndFlush(request).sync();

            // Use lock to wait for the response
            synchronized (lock) {
                lock.wait();
            }

            // After get the response, free all the source
            RpcMessage response = resultHandler.getResponse();
            if(response != null) {
                future.channel().closeFuture().sync();
            }

            return response;
        } finally {
            group.shutdownGracefully();
        }
    }

    /**
     * Handle the response message. When get the response, notify the waiting thread to continue.
     */
    class ResultHandler extends ChannelInboundHandlerAdapter {
        private RpcMessage response;

        public RpcMessage getResponse() {
            return response;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            this.response = (RpcMessage) msg;

            // Get the response, and it should notify the waiting thread
            synchronized (lock) {
                lock.notifyAll();
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            LOGGER.error("Client exception is general");
        }
    }
}
