package com.rpc2.zl.remoting.transport.netty;

import com.rpc2.zl.common.constant.Constants;
import com.rpc2.zl.remoting.serialize.SerializerInstance;
import com.rpc2.zl.remoting.transport.RpcConnector;
import com.rpc2.zl.rpc.codec.RpcDecoder;
import com.rpc2.zl.rpc.codec.RpcEncoder;
import com.rpc2.zl.rpc.common.keepalive.ClientHeartbeatHandler;
import com.rpc2.zl.rpc.config.ClientConfig;
import com.rpc2.zl.rpc.protocol.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Getter;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Netty是一个基于NIO的Java网络编程框架，具有并发高、传输快、封装好的特点
 * SimpleChannelInboundHandler是继承自ChannelInboundHandlerAdapter，且SimpleChannelInboundHandler支持入站的消息用泛型表示
 * Created by Vincent on 2018/7/13.
 */
public class NettyRpcConnector extends AbstractHandler<RpcMessage> implements RpcConnector {

    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    @Getter
    private volatile Channel channel;

    private ConcurrentHashMap<String, RpcResponseFuture> pendingRPC = new ConcurrentHashMap<>();

    /**
     * 当channel注册完成后，会出发channelRegistered方法
     * @param ctx
     * @throws Exception
     */
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    public void connect(ClientConfig config) {
        InetSocketAddress remotePeer = new InetSocketAddress(config.getRemoteHost(), config.getRemotePort());
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new initializer())
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int)config.getConnectTimeoutMillis());
        ChannelFuture channelFuture = bootstrap.connect(remotePeer);
        // 连接成功的操作
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(channelFuture.isSuccess()) {
//                    NettyRpcConnector connectedHandler = channelFuture.channel().pipeline().get(NettyRpcConnector.class);
//                    RpcClientInvokerManager.addConnectedHandler(handler);
                }
            }
        });
    }

    @Override
    public RpcResponseFuture send(RpcRequest request) {
        RpcResponseFuture responseFuture = new RpcResponseFuture(request);
        pendingRPC.put(request.getRequestId(), responseFuture);
        RpcMessage message=new RpcMessage();
        byte[] data = SerializerInstance.getInstance().serialize(request);
        RpcMessageHeader messageHeader=new RpcMessageHeader();
        messageHeader.setMsgLength(data.length);
        message.setHeader(messageHeader);
        message.setBody(request);
        channel.writeAndFlush(message);
        return responseFuture;
    }

    @Override
    public void close(Object obj) {
        Channel channel = (Channel) obj;
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    public void closeAll() {
        this.eventLoopGroup.shutdownGracefully();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMessage message) {
        RpcResponse response = (RpcResponse) message.getBody();
        String requestId = response.getRequestId();
        RpcResponseFuture responseFuture = pendingRPC.get(requestId);
        if(responseFuture != null) {
            pendingRPC.remove(requestId);
            responseFuture.setResponse(response);
        }
    }

    class initializer extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            ChannelPipeline cp = socketChannel.pipeline();

            cp.addLast(new RpcEncoder(RpcRequest.class))
                    .addLast(new RpcDecoder(RpcResponse.class))
                    .addLast(new IdleStateHandler(0, 0, Constants.ALLIDLE_TIME_SECONDS))
                    .addLast(new ClientHeartbeatHandler())
                    .addLast(new NettyRpcConnector());
        }
    }
}
