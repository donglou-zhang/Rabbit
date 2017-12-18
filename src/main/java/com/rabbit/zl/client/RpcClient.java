package com.rabbit.zl.client;

import com.rabbit.zl.clientStub.ClientRpcInvoker;
import com.rabbit.zl.rpc.registry.RpcDiscovery;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbit.zl.rpc.invoke.RpcInvoker;
import com.rabbit.zl.rpc.transmission.DefaultRpcConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
@Component("rpcClient")
public class RpcClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcClient.class);

    @Getter @Setter private RpcProxyFactory proxyFactory;

    @Getter @Setter private RpcInvoker invoker;

    @Getter @Setter private DefaultRpcConnector connector;

    @Getter @Setter
    private RpcDiscovery serviceDiscovery;

    @Autowired
    public RpcClient(RpcDiscovery discovery) {
        this.serviceDiscovery = discovery;

        proxyFactory = new DefaultRpcProxyFactory();
        invoker = new ClientRpcInvoker();
        connector = new DefaultRpcConnector();
        init();
    }

    public void init() {
        proxyFactory.setInvoker(invoker);
        invoker.setDiscovery(serviceDiscovery);
        invoker.setConnector(connector);

        LOGGER.debug("[RABBIT] Rpc client init complete.");
    }

    /**
     * User invoke this method to send request to remote server. After receiving request, the server will handle the request
     * Then server will send result back to client.
     * Client gets the result just like invoking local method
     *
     * @param rpcInterface
     * @param <T>
     * @return
     */
    public <T> T getBean(Class<T> rpcInterface) {
        return proxyFactory.getProxy(rpcInterface);
    }
}
