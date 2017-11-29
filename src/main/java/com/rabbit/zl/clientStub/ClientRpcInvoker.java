package com.rabbit.zl.clientStub;

import com.rabbit.zl.common.exception.RpcException;
import com.rabbit.zl.rpc.registry.RpcDiscovery;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbit.zl.rpc.invoke.AbstractRpcInvoker;
import com.rabbit.zl.rpc.invoke.RpcContext;
import com.rabbit.zl.rpc.protocol.model.RpcMessage;
import com.rabbit.zl.rpc.transmission.RpcConnector;

import java.net.InetSocketAddress;
import java.util.List;

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

    @Getter @Setter private RpcDiscovery discovery;

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

            //TODO discover the service provider ip address, get the first, don't consider loading balance
            List<String> services = discovery.discoverAll("Rabbit", request.getRpcInterface().getName());
            if(services.size() > 0) {
                String[] parts = services.get(0).split("/");
                String address = parts[parts.length-2].split("&")[1];
                String host = address.split(":")[0].trim();
                int port = Integer.parseInt(address.split(":")[1].trim());
                request.setServerAddress(new InetSocketAddress(host, port));
                LOGGER.debug("[RABBIT] Rpc client invoker is invoking, | request={}, async={}", request, async);
                return connector.send(request, async);
            }else {
                throw new RpcException("Can not find the service provider");
            }

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
