package client;

import clientStub.DefaultRpcInvoker;
import lombok.Getter;
import lombok.Setter;
import rpc.protocol.model.RpcMessage;

/**
 * Rpc Client to send Rpc request
 *
 * @author Vincent
 * Created  on 2017/11/10.
 */
public class RpcClient {

    @Getter @Setter private String remoteHost;

    @Getter @Setter private int remotePort;

    @Getter @Setter private DefaultRpcInvoker invoker;

    public RpcClient() {

    }

    public RpcClient(String host, int port) {
        this.remoteHost = host;
        this.remotePort = port;
    }

    public RpcMessage send(RpcMessage request) throws Exception{
        return null;
    }
}
