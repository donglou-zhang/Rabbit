package com.rpc2.zl.remoting.transport.netty;

import com.rpc2.zl.remoting.serialize.SerializerInstance;
import com.rpc2.zl.rpc.common.RpcInvoker;
import com.rpc2.zl.rpc.protocol.RpcMessage;
import com.rpc2.zl.rpc.protocol.RpcMessageHeader;
import com.rpc2.zl.rpc.protocol.RpcRequest;
import com.rpc2.zl.rpc.protocol.RpcResponse;
import com.rpc2.zl.server.RpcServerInvoker;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.Executor;

/**
 * Created by Vincent on 2018/7/21.
 */
public class NettyRpcAcceptorHandler extends AbstractHandler<RpcMessage> {

    private final Executor executor;

    private RpcServerInvoker serverInvoker;

    public NettyRpcAcceptorHandler(Executor executor, RpcServerInvoker invoker) {
        this.executor = executor;
        this.serverInvoker = invoker;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMessage message) {
        this.executor.execute(new Runnable() {
            @Override
            public void run() {
                RpcInvoker invoker = serverInvoker.buildRpcInvokerChain(serverInvoker);
                RpcResponse responseBody = (RpcResponse) invoker.invoke(serverInvoker.buildRpcInvocation((RpcRequest) message.getBody()));
                RpcMessage responseMessage = new RpcMessage();
                byte[] responseBodyData = SerializerInstance.getInstance().serialize(responseBody);
                RpcMessageHeader responseHeader = new RpcMessageHeader();
                responseHeader.setMsgLength(responseBodyData.length);
                responseMessage.setBody(responseBodyData);
                responseMessage.setHeader(responseHeader);
                ctx.writeAndFlush(responseMessage);
            }
        });
    }
}
