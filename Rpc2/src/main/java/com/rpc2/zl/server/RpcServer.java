package com.rpc2.zl.server;

import com.rpc2.zl.remoting.transport.RpcAcceptor;
import com.rpc2.zl.remoting.transport.netty.NettyRpcAcceptor;
import com.rpc2.zl.rpc.common.utils.ActiveFilterUtil;
import com.rpc2.zl.rpc.config.ServerConfig;

/**
 * Created by Vincent on 2018/7/18.
 */
public class RpcServer{

    private ServerConfig serverConfig;

    private RpcServerInitializer initializer;

    public RpcServer(ServerConfig serverConfig) {
        this.initializer = new RpcServerInitializer();
        this.serverConfig = serverConfig;
        new NettyRpcAcceptor(new RpcServerInvoker(initializer.getHandlerMap(), ActiveFilterUtil.getFilterMap(true))).bind(serverConfig);
    }
}
