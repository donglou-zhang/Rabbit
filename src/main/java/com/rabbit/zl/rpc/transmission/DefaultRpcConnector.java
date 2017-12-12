package com.rabbit.zl.rpc.transmission;

import com.rabbit.zl.common.exception.RpcException;
import com.rabbit.zl.rpc.protocol.model.RpcMessage;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbit.zl.transfer.netty.NettyClientConnector;

/**
 * Default connector, used in the client side, in charge of sending data to the server
 * According to configuration, use different kinds of Connector
 *
 * @author Vincent
 * Created on 2017/11/17.
 */
public class DefaultRpcConnector extends AbstractRpcConnector{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRpcConnector.class);

    private RpcConnector connector;

    @Getter @Setter private String remoteHost;

    @Getter @Setter private int remotePort;

    public DefaultRpcConnector() {}

    public DefaultRpcConnector(String host, int port) {
        this.remoteHost = host;
        this.remotePort = port;
    }

    private void init() {
        this.connector = new NettyClientConnector(remoteHost, remotePort);
        System.out.println("DefaultRpcConnector: init connector");
        LOGGER.debug("Get the NettyClientConnector with remote address[{}:{}]", remoteHost, remotePort);
    }

    public RpcMessage send(RpcMessage request, boolean async) throws RpcException {
        if(connector == null) {
            init();
        }

        return connector.send(request, async);
    }
}
