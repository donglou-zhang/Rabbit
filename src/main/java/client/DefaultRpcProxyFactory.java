package client;

import lombok.Getter;
import rpc.invoke.RpcInvocationHandler;
import rpc.invoke.RpcInvoker;

import java.lang.reflect.Proxy;

public class DefaultRpcProxyFactory implements RpcProxyFactory{

    @Getter private RpcInvoker invoker;

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> rpcInterface) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[] {rpcInterface}, new RpcInvocationHandler(invoker));
    }

    @Override
    public void setInvoker(RpcInvoker invoker) {
        this.invoker = invoker;
    }
}
