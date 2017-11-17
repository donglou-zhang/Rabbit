package rpc.transmission;

import common.exception.RpcException;
import lombok.Getter;
import rpc.protocol.model.RpcMessage;

/**
 * @author Vincent
 * Created on 2017/11/13.
 */
public abstract class AbstractRpcConnector implements RpcConnector{

    @Getter private String remoteHost;

    @Getter private int remotePort;

    public AbstractRpcConnector() {

    }

    public void setRemoteAddress(String remoteHost, int remotePort) {
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    public long connect() throws RpcException {
        return 0;
    }

    public boolean disconnect(long connectionId) {
        return false;
    }

    public RpcMessage send(RpcMessage request, boolean async) throws RpcException {
        return null;
    }

    public int getRpcTimeoutInMillis() {
        return 0;
    }
}
