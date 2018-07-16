package com.rpc2.zl.remoting.transport.netty;

import com.rpc2.zl.client.RpcClientInvoker;
import com.rpc2.zl.common.constant.Constants;
import com.rpc2.zl.remoting.transport.RpcConnector;
import com.rpc2.zl.rpc.codec.RpcDecoder;
import com.rpc2.zl.rpc.codec.RpcEncoder;
import com.rpc2.zl.rpc.common.keepalive.ClientHeartbeatHandler;
import com.rpc2.zl.rpc.common.utils.ActiveFilterUtil;
import com.rpc2.zl.rpc.config.ClientConfig;
import com.rpc2.zl.rpc.filter.RpcFilter;
import com.rpc2.zl.rpc.protocol.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Getter;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Vincent on 2018/7/13.
 */
public class NettyRpcConnector extends SimpleChannelInboundHandler<RpcMessage> implements RpcConnector {

    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    @Getter
    private volatile Channel channel;

    private ConcurrentHashMap<String, RpcResponseFuture> pendingRPC = new ConcurrentHashMap<>();

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
    }

    @Override
    public RpcResponseFuture send(RpcRequest request) {
        RpcResponseFuture responseFuture = new RpcResponseFuture(request);
        pendingRPC.put(request.getRequestId(), responseFuture);
        RpcMessage message = new RpcMessage();
        //TODO according to conf, get serialize util
        byte[] data = null;

        RpcMessageHeader messageHeader = new RpcMessageHeader();
        messageHeader.setMsgLength(data.length);
        message.setHeader(messageHeader);
        message.setBody(request);
        channel.writeAndFlush(message);
        return responseFuture;
    }

    /**
     * Once get the response, execute the corresponding action
     * @param channelHandlerContext
     * @param message
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcMessage message) throws Exception {
        RpcResponse response = (RpcResponse) message.getBody();
        String requestId = response.getRequestId();
        RpcResponseFuture responseFuture = pendingRPC.get(requestId);
        if(responseFuture != null) {
            pendingRPC.remove(requestId);
            responseFuture.executeAfterDone(response);
        }
    }

    class initializer extends ChannelInitializer<SocketChannel> {

        private Map<String, RpcFilter> getFilterMap() {
            return ActiveFilterUtil.getFilterMap(false);
        }

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            ChannelPipeline cp = socketChannel.pipeline();

            cp.addLast(new RpcEncoder(RpcRequest.class)).addLast(new RpcDecoder(RpcResponse.class))
                    .addLast(new IdleStateHandler(0, 0, Constants.ALLIDLE_TIME_SECONDS))
                    .addLast(new ClientHeartbeatHandler())
                    .addLast(new RpcClientInvoker(this.getFilterMap()));
        }
    }
}
