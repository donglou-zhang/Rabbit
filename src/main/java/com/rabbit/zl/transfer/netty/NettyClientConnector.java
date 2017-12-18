package com.rabbit.zl.transfer.netty;

import com.rabbit.zl.common.exception.RpcException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbit.zl.rpc.protocol.codec.NettyRpcDecoder;
import com.rabbit.zl.rpc.protocol.codec.NettyRpcEncoder;
import com.rabbit.zl.rpc.protocol.model.RpcMessage;
import com.rabbit.zl.rpc.transmission.AbstractRpcConnector;

/**
 * Connector: client side
 * Use netty to send data to server and wait for response
 *
 * @author Vincent
 * Created  on 2017/11/13.
 */
public class NettyClientConnector extends AbstractRpcConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyClientConnector.class);

    private static final Object lock  = new Object();

    private String remoteHost;

    private int remotePort;

    private ChannelFuture future;
    private Bootstrap bootstrap;
    private ResultHandler resultHandler;

    public NettyClientConnector() {}

    public NettyClientConnector(String host, int port) {
        this.remoteHost = host;
        this.remotePort = port;

        init();
    }

    private void init() {
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        resultHandler = new ResultHandler();
        try {
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline cp = socketChannel.pipeline();
                            cp.addLast("RpcMessageEncoder", new NettyRpcEncoder(RpcMessage.class));
                            cp.addLast("RpcMessageDecoder", new NettyRpcDecoder(RpcMessage.class));
                            cp.addLast("TimeoutHandler", new ReadTimeoutHandler(3));
                            cp.addLast("ResultHandler", resultHandler);
                        }
                    });
            connect();
        }catch (Exception e) {
            e.printStackTrace();
        }
//        finally {
//            group.shutdownGracefully();
//        }
    }

    public RpcMessage send(RpcMessage request, boolean async) throws RpcException{
            RpcMessage response = null;

            if(this.future == null || !this.future.channel().isActive()) {
                connect();
            }

            try {
                future.channel().writeAndFlush(request).sync();

                // Use lock to wait for the response
                synchronized (lock) {
                    lock.wait();
                }
                // After get the response, free all the source
                response = resultHandler.getResponse();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return response;
    }

    public long connect() {
        try {
            if(this.future == null || !this.future.channel().isActive()) {
                this.future = bootstrap.connect(this.remoteHost, this.remotePort).sync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Handle the response message. When get the response, notify the waiting thread to continue.
     */
    class ResultHandler extends ChannelInboundHandlerAdapter {
        private RpcMessage request;
        private RpcMessage response;

        public ResultHandler() {}

        public ResultHandler(RpcMessage request) {
            this.request = request;
        }

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

        /**
         *  After connect successful, send request to server
         */
//        @Override
//        public void channelActive(ChannelHandlerContext ctx) throws InterruptedException {
//            ctx.writeAndFlush(this.request);
//            System.out.println("NettyClientConnector: send the request[" + request.toString() + "]");
//        }

        public void channelReadCompelete(ChannelHandlerContext ctx) {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            System.out.println("Client exception is general");
            LOGGER.error("Client exception is general");
        }
    }
}
