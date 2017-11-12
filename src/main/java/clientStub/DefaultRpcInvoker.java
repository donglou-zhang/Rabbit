package clientStub;

import rpc.protocol.model.RpcMessage;
import rpc.registry.RpcRegistry;
import rpc.transmission.RpcConnector;

/**
 * @author Vincent
 * Created  on 2017/11/12.
 */
public class DefaultRpcInvoker extends AbstractRpcInvoker {

    public RpcMessage invoke(RpcMessage request) {
        return null;
    }

    @Override
    public void setConnector(RpcConnector connector) {

    }

    @Override
    public void setRegistry(RpcRegistry registry) {

    }
}
