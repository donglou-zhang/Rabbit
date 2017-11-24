package com.rabbit.zl.client;

import com.rabbit.zl.client.clientStub.ClientRpcInvoker;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbit.zl.rpc.invoke.RpcInvoker;
import com.rabbit.zl.rpc.transmission.DefaultRpcConnector;

/**
 * Rpc Client provides the method to get remote service instance
 * It's invocation sequence like below:
 * (1) RpcClient: getBean()
 * (2) DefaultRpcProxyFactory: getProxy()
 * (3) RpcInvocationHandler: invoke()
 * (4) ClientRpcInvoker: invoke()
 * (5) RpcConnector: send()
 *
 * @author Vincent
 * Created  on 2017/11/10.
 */
public class RpcClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcClient.class);

    @Getter @Setter private String remoteHost;

    @Getter @Setter private int remotePort;

    @Getter @Setter private RpcProxyFactory proxyFactory;

    @Getter @Setter private RpcInvoker invoker;

    @Getter @Setter private DefaultRpcConnector connector;

    public RpcClient() {
        //TODO not set the remoteHost and remotePort.
        proxyFactory = new DefaultRpcProxyFactory();
        invoker = new ClientRpcInvoker();
        connector = new DefaultRpcConnector();
    }

    public void init() {
        proxyFactory.setInvoker(invoker);
        connector.setRemoteHost(remoteHost);
        connector.setRemotePort(remotePort);

        LOGGER.debug("[RABBIT] Rpc client init complete.");
    }

    public <T> T getBean(Class<T> rpcInterface) {
        return proxyFactory.getProxy(rpcInterface);
    }
}