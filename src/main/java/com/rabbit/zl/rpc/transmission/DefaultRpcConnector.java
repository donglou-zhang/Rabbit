package com.rabbit.zl.rpc.transmission;

import com.rabbit.zl.common.exception.RpcException;
import com.rabbit.zl.rpc.protocol.model.RpcMessage;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbit.zl.transfer.netty.NettyClientConnector;

/**
 * @author Vincent
 * Created on 2017/11/17.
 */
public class DefaultRpcConnector{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRpcConnector.class);

    private RpcConnector connector;

    @Getter @Setter private String remoteHost;

    @Getter @Setter private int remotePort;

    public DefaultRpcConnector() {
        this.connector = new NettyClientConnector(remoteHost, remotePort);
    }

    public DefaultRpcConnector(String host, int port) {
        //TODO according to configuration, choose the right connector
        this.remoteHost = host;
        this.remotePort = port;
        connector = new NettyClientConnector(remoteHost, remotePort);
    }

    public RpcMessage send(RpcMessage request, boolean async) throws RpcException {
        return connector.send(request, async);
    }
}
