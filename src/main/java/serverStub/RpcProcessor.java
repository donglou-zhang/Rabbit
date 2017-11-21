package serverStub;

import rpc.invoke.RpcInvoker;
import rpc.protocol.model.RpcMessage;
import rpc.transmission.RpcChannel;

/**
 * @author Vincent
 * Created on 2017/11/14.
 */
public interface RpcProcessor {

    RpcMessage process(RpcMessage request, RpcChannel channel);

    void setInvoker(RpcInvoker invoker);
}
