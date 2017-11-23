package com.rabbit.zl.client.clientStub;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbit.zl.rpc.invoke.AbstractRpcInvoker;
import com.rabbit.zl.rpc.invoke.RpcContext;
import com.rabbit.zl.rpc.protocol.model.RpcMessage;
import com.rabbit.zl.rpc.transmission.RpcConnector;

/**
 * RpcInvoker takes charge of using RpcConnector to maintain the channel between client and server
 * It uses RpcProtocol to encode request message and then send to server through channel, wait for response
 *
 * @author Vincent
 * Created  on 2017/11/12.
 */
public class ClientRpcInvoker extends AbstractRpcInvoker {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientRpcInvoker.class);

    @Getter @Setter private RpcConnector connector;

    @Override
    public RpcMessage invoke(RpcMessage request) {
        try {
            //the context of current thread
            RpcContext ctx = RpcContext.getRpcContext();
            request.setRpcTimeoutInMillis(checkRpcTimeoutInMillis(ctx));
            request.setOneWay(ctx.isOneWay());
            request.setRpcAttachments(ctx.getRpcAttachments());
            request.setRpcId(ctx.getRpcId());
            boolean async = ctx.isAsync();
            LOGGER.debug("[RABBIT] Rpc client invoker is invoking, | request={}, async={}", request, async);
            return connector.send(request, async);
        } finally {
            RpcContext.removeRpcContext();
        }
    }

    /**
     * Check and set the rpc time out in millis
     *
     * @param ctx
     * @return
     */
    private int checkRpcTimeoutInMillis(RpcContext ctx) {
        //Get timeout from rpcContext in this invocation
        int timeout = ctx.getRpcTimeoutInMillis();
        if(timeout > 0)
            return timeout;

        //if does not set timeout in this invocation, get from global setting
        timeout = connector.getRpcTimeoutInMillis();
        if(timeout > 0)
            return timeout;

        timeout = Integer.MAX_VALUE;
        return timeout;
    }
}
