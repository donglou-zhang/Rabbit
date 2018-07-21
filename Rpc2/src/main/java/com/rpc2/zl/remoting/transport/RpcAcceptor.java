package com.rpc2.zl.remoting.transport;

import com.rpc2.zl.rpc.config.ServerConfig;

/**
 * Created by Vincent on 2018/7/18.
 */
public interface RpcAcceptor {

    void bind(ServerConfig config);
}
