package com.rabbit.zl.rpc.invoke;

import com.rabbit.zl.common.exception.RpcException;
import com.rabbit.zl.rpc.registry.RpcDiscovery;
import com.rabbit.zl.rpc.registry.RpcRegistry;
import com.rabbit.zl.rpc.transmission.RpcConnector;
import com.rabbit.zl.rpc.protocol.model.RpcMessage;

import java.util.Map;

/**
 * Rpc invoker, launch the rpc invocation
 *
 * @author Vincent
 * Created  on 2017/11/12.
 */
public interface RpcInvoker {

    /**
     * Invoke on the client or server side
     *
     * <b>Client side<b/>
     * Encode the client request and send to the rpc server, wait the response
     *
     * <b>Server side<b/>
     * Decode the rpc request and invoke the right method of rpc api implementor
     *
     * @param request
     * @return
     */
    RpcMessage invoke(RpcMessage request) throws RpcException;

    /**
     * Set rpc connector.
     * Only client side invoker need implement this method
     *
     * @param connector
     */
    void setConnector(RpcConnector connector);

    /**
     * Set rpc registry
     * Used in the server side
     *
     *
     * @param registry
     */
    void setRegistry(RpcRegistry registry);

    /**
     * Set rpc discovery
     * Used in the client side
     *
     * @param discovery
     */
    void setDiscovery(RpcDiscovery discovery);

    /**
     * Set the rpcInterface and its implementation bean mapping.
     *
     * @param map
     */
    void setServiceBeanMap(Map<String, Object> map);
}
