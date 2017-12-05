package com.rabbit.zl.transfer.netty;

import com.rabbit.zl.common.exception.RpcException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbit.zl.rpc.protocol.codec.NettyRpcDecoder;
import com.rabbit.zl.rpc.protocol.codec.NettyRpcEncoder;
import com.rabbit.zl.rpc.protocol.model.RpcMessage;
import com.rabbit.zl.rpc.transmission.AbstractRpcConnector;

/**
 *
 * @author Vincent
 * Created  on 2017/11/13.
 */
public class NettyClientConnector extends AbstractRpcConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyClientConnector.class);

    private static final Object lock  = new Object();

    private String remoteHost;

    private int remotePort;

    public NettyClientConnector() {}

    public NettyClientConnector(String host, int port) {
        this.remoteHost = host;
        this.remotePort = port;
    }

    public RpcMessage send(RpcMessage request, boolean async) throws RpcException {
        EventLoopGroup group = new NioEventLoopGroup();
        final ResultHandler resultHandler = new ResultHandler();
        RpcMessage response = null;
        try {
            Bootstrap bs = new Bootstrap();
            bs.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline cp = socketChannel.pipeline();
                            cp.addLast("RpcMessageEncoder", new NettyRpcEncoder(RpcMessage.class));
                            cp.addLast("RpcMessageDecoder", new NettyRpcDecoder(RpcMessage.class));
                            cp.addLast("ResultHandler", resultHandler);
                        }
                    });

            ChannelFuture future = bs.connect(this.remoteHost, this.remotePort).sync();
            System.out.println("NettyClientConnector: connect["+this.remoteHost+":"+this.remotePort+"]");
            future.channel().writeAndFlush(request).sync();
            System.out.println("NettyClientConnector: send request["+request.toString()+"]");

            // Use lock to wait for the response
            synchronized (lock) {
                lock.wait();
            }

            // After get the response, free all the source
            response = resultHandler.getResponse();
            if(response != null) {
                future.channel().closeFuture().sync();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
        return response;
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
            System.out.println("NettyClientConnector: channelRead");
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
