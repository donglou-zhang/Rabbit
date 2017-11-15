package serverStub;

import rpc.protocol.model.RpcMessage;

/**
 * @author Vincent
 * Created on 2017/11/14.
 */
public interface RpcProcessor {

    void process(RpcMessage request);
}
