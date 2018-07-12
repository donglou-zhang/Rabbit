package com.rpc2.zl.client;

import com.rpc2.zl.rpc.config.ClientConfig;
import com.rpc2.zl.rpc.proxy.RpcProxy;

import java.lang.reflect.Proxy;

/**
 * Created by Vincent on 2018/7/10.
 */
public class RpcClient {

    private ClientConfig clientConfig;

    public RpcClient() {
        clientConfig = new ClientConfig();
    }

    public <T> T createProxy(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, new RpcProxy(clientConfig));
    }

    public <T> T createProxy(Class<T> interfaceClass, RpcReference reference) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, new RpcProxy(clientConfig, reference));
    }
}
