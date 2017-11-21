package rpc.transmission;

import lombok.Getter;
import lombok.Setter;
import serverStub.RpcProcessor;

/**
 * @author Vincent
 * Created on 2017/11/17.
 */
public class AbstractRpcAcceptor implements RpcAcceptor{

    @Override
    public void close() {

    }

    @Override
    public void setProcessor(RpcProcessor processor) {

    }

    @Override
    public void setConnections(int connections) {

    }

    @Override
    public void setAddress(String host, int port) {

    }
}
