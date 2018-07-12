package com.rpc2.zl.rpc.proxy;

import com.rpc2.zl.client.RpcReference;
import com.rpc2.zl.rpc.config.ClientConfig;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by Vincent on 2018/7/10.
 */
public class RpcProxy implements InvocationHandler {

    private ClientConfig clientConfig = null;

    private RpcReference reference = null;

    public RpcProxy(ClientConfig config) {
        this.clientConfig = config;
    }

    public RpcProxy(ClientConfig config, RpcReference reference) {
        this.clientConfig = config;
        this.reference = reference;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcHystrixCommand command = new RpcHystrixCommand(method, args, reference, clientConfig);
        return command.execute();
    }
}
