package rpc.transmission;

import common.exception.RpcException;
import rpc.protocol.model.RpcMessage;

/**
 * @author Vincent
 * Created on 2017/11/13.
 */
public interface RpcConnector {

    long connect() throws RpcException;

    boolean disconnect(long connectionId);

    /**
     * Send rpc message and wait return rpc response message
     * if 'async' is set true and return <tt>null<tt/>
     *
     * @param request
     * @param async
     * @return
     * @throws RpcException
     */
    RpcMessage send(RpcMessage request, boolean async) throws Exception;

    /**
     * @return global setting rpc timeout in millis
     */
    int getRpcTimeoutInMillis();
}
