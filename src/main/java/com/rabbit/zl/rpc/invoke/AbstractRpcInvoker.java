package com.rabbit.zl.rpc.invoke;

import com.rabbit.zl.rpc.registry.RpcRegistry;
import com.rabbit.zl.rpc.transmission.RpcConnector;

import java.util.Map;

/**
 *
 * @author Vincent
 * Created  on 2017/11/12.
 */
public abstract class AbstractRpcInvoker implements RpcInvoker {

    @Override
    public void setConnector(RpcConnector connector) {}

    @Override
    public void setRegistry(RpcRegistry registry) {}

    @Override
    public void setServiceBeanMap(Map<String, Object> map) {}
}
