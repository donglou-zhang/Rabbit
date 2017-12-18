package com.rabbit.zl.serverStub;

import com.rabbit.zl.rpc.invoke.RpcInvoker;
import com.rabbit.zl.rpc.protocol.model.RpcMessage;
import com.rabbit.zl.rpc.transmission.RpcChannel;

/**
 * Server side will dispatch resource(threads pool) to handle the request
 *
 * @author Vincent
 * Created on 2017/11/14.
 */
public interface RpcProcessor {

    RpcMessage process(RpcMessage request, RpcChannel channel);

    void setInvoker(RpcInvoker invoker);
}
