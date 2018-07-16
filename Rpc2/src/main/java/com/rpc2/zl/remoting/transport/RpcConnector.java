package com.rpc2.zl.remoting.transport;

import com.rpc2.zl.rpc.config.ClientConfig;
import com.rpc2.zl.rpc.protocol.RpcRequest;
import com.rpc2.zl.rpc.protocol.RpcResponseFuture;

/**
 * Created by Vincent on 2018/7/13.
 */
public interface RpcConnector {

    void connect(ClientConfig config);

    RpcResponseFuture send(RpcRequest request);
}
