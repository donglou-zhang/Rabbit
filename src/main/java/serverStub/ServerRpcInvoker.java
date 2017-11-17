package serverStub;

import common.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.invoke.RpcInvoker;
import rpc.protocol.model.RpcMessage;
import rpc.registry.RpcRegistry;
import rpc.transmission.RpcConnector;

/**
 *
 *
 *
 * @author Vincent
 * Created  on 2017/11/16.
 */
public class ServerRpcInvoker implements RpcInvoker {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerRpcInvoker.class);


    @Override
    public RpcMessage invoke(RpcMessage request) throws RpcException {
        return null;
    }

    @Override
    public void setConnector(RpcConnector connector) {

    }

    @Override
    public void setRegistry(RpcRegistry registry) {

    }
}
