package com.rabbit.zl.client;

import com.rabbit.zl.rpc.invoke.RpcInvoker;

/**
 * Rpc proxy factory can "produce": the instance of proxy class for the specified interface
 *
 * @author Vincent
 * Created  on 2017/11/17.
 */
public interface RpcProxyFactory {

    <T> T getProxy(Class<T> rpcInterface);

    void setInvoker(RpcInvoker invoker);
}
