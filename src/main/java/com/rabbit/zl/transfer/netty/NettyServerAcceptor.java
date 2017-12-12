package com.rabbit.zl.transfer.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbit.zl.rpc.protocol.codec.NettyRpcDecoder;
import com.rabbit.zl.rpc.protocol.codec.NettyRpcEncoder;
import com.rabbit.zl.rpc.protocol.model.RpcMessage;
import com.rabbit.zl.rpc.transmission.AbstractRpcAcceptor;
import com.rabbit.zl.rpc.transmission.DefaultRpcChannel;
import com.rabbit.zl.serverStub.RpcProcessor;

/**
 *
 * @author Vincent
 * Created  on 2017/11/13.
 */
public class NettyServerAcceptor extends AbstractRpcAcceptor{

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServerAcceptor.class);

    @Getter @Setter private String serverHost;

    @Getter @Setter private int serverPort;

    @Getter private RpcProcessor processor;

    public NettyServerAcceptor() {}

    public NettyServerAcceptor(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public void setProcessor(RpcProcessor processor) {
        this.processor = processor;
    }

    public NettyServerAcceptor(String serverHost, int serverPort, RpcProcessor processor) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.processor = processor;
    }

    public void listen() {
        System.out.println("NettyServerAcceptor: carry out listen()");
        //to accept the incoming connections, once it get the connection,it will registered it to workerGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();//to handle the accepted connection

        ServerResultHandler resultHandler = new ServerResultHandler();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();//an assistant bootstrap class
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel channel) throws Exception {
                    channel.pipeline().addLast("RpcMessageEncoder", new NettyRpcEncoder(RpcMessage.class))
                            .addLast("RpcMessageDecoder", new NettyRpcDecoder(RpcMessage.class))
                            .addLast("resultHandler", resultHandler);
                }
            }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(serverHost, serverPort).sync();
            System.out.println("NettyServerAcceptor: start listening address["+serverHost+" : "+serverPort+"] ...");
            LOGGER.debug("Server started on address [{} : {}]", serverHost, serverPort);

            future.channel().closeFuture().sync();
        } catch (Exception e) {
            LOGGER.error("Server start error!");
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public void setAddress(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    //TODO explain how the response message of server return back to client through netty
    class ServerResultHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("NettyServerAcceptor: Before processing, request message[" + msg.toString() + "]");
            RpcMessage response = processor.process((RpcMessage) msg, new DefaultRpcChannel());
            //In server side, ChannelFutureListener.CLOSE will close connection with client initiative
//            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            ctx.writeAndFlush(response);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            LOGGER.error("Server result handler error");
            ctx.close();
        }
    }
}
