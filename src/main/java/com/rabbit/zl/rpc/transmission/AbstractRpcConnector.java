package com.rabbit.zl.rpc.transmission;

import com.rabbit.zl.common.exception.RpcException;
import com.rabbit.zl.rpc.protocol.model.RpcMessage;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Vincent
 * Created on 2017/11/13.
 */
public abstract class AbstractRpcConnector implements RpcConnector{

    @Getter @Setter public String remoteHost;

    @Getter @Setter public int remotePort;

    public AbstractRpcConnector() {}

    public AbstractRpcConnector(String remoteHost, int remotePort) {
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
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

    public RpcMessage send(RpcMessage request, boolean async) throws RpcException{
        return null;
    }

    public int getRpcTimeoutInMillis() {
        return 0;
    }
}
