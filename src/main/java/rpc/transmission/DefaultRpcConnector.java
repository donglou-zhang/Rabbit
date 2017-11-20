package rpc.transmission;

import common.exception.RpcException;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.protocol.model.RpcMessage;
import transfer.netty.NettyClientConnector;

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
