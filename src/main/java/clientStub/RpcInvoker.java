package clientStub;

import common.exception.RpcException;
import rpc.protocol.model.RpcMessage;
import rpc.registry.RpcRegistry;
import rpc.transmission.RpcConnector;

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
     * Set rpc connector. Only client side invoker need implement this method
     *
     * @param connector
     */
    void setConnector(RpcConnector connector);

    /**
     * Set rpc registry
     *
     * @param registry
     */
    void setRegistry(RpcRegistry registry);
}
