package com.rpc2.zl.remoting.transport.netty;

import com.rpc2.zl.common.constant.ConstantConfig;
import com.rpc2.zl.common.constant.Constants;
import com.rpc2.zl.common.threadPool.ThreadPoolFactory;
import com.rpc2.zl.remoting.transport.RpcAcceptor;
import com.rpc2.zl.rpc.codec.RpcDecoder;
import com.rpc2.zl.rpc.codec.RpcEncoder;
import com.rpc2.zl.rpc.common.keepalive.ServerHeartbeatHandler;
import com.rpc2.zl.rpc.config.ServerConfig;
import com.rpc2.zl.rpc.protocol.RpcMessage;
import com.rpc2.zl.rpc.protocol.RpcRequest;
import com.rpc2.zl.rpc.protocol.RpcResponse;
import com.rpc2.zl.server.RpcServerInvoker;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Executor;

/**
 * Created by Vincent on 2018/7/18.
 */
public class NettyRpcAcceptor implements RpcAcceptor{

    @Autowired
    private ThreadPoolFactory threadPoolFactory;

    private RpcServerInvoker serverInvoker;

    public NettyRpcAcceptor() {}

    public NettyRpcAcceptor(RpcServerInvoker serverInvoker) {
        this.serverInvoker = serverInvoker;
    }

    public void bind(ServerConfig config) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new initializer())
                    .childOption(ChannelOption.SO_BACKLOG, 128) // 当多个客户端到来时，服务端无法处理，则将其放在队列中等待处理，backlog指定了队列的大小
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            try {
                ChannelFuture channelFuture = bootstrap.bind(config.getServerHost(), config.getServerPort()).sync();
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    class initializer extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            ChannelPipeline pipeline = socketChannel.pipeline();
            Executor executor = threadPoolFactory.getThreadPool(ConstantConfig.DEFAULT_THREAD_POOL_NAME).getExecutor(1,1);
            pipeline.addLast(new RpcEncoder(RpcResponse.class))
                    .addLast(new RpcDecoder(RpcRequest.class))
                    .addLast(new IdleStateHandler(Constants.READER_TIME_SECONDS, 0, 0))
                    .addLast(new ServerHeartbeatHandler())
                    .addLast(new NettyRpcAcceptorHandler(executor, serverInvoker));
        }
    }
}
