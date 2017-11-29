package com.rabbit.zl.client;

import lombok.Getter;
import com.rabbit.zl.rpc.invoke.RpcInvocationHandler;
import com.rabbit.zl.rpc.invoke.RpcInvoker;
import lombok.Setter;

import java.lang.reflect.Proxy;

/**
 * Proxy factory, provides all kinds of proxy of class.
 * (1)invoke getProxy(Class rpcInterface), get the instance of class, like demoService
 * (2)invoke the method that we need, like demoService.hello(String str)
 * (3)When we invoke the proxy instance's method, just like hello() above, it will actually invoke RpcInvocationHandler's invoke() method
 *
 * @author Vincent
 * Created  on 2017/11/10.
 */
public class DefaultRpcProxyFactory implements RpcProxyFactory{

    @Getter @Setter private RpcInvoker invoker;

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> rpcInterface) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[] {rpcInterface}, new RpcInvocationHandler(invoker));
    }
}
