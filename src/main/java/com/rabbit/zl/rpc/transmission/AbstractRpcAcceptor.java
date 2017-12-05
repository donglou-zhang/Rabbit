package com.rabbit.zl.rpc.transmission;

import com.rabbit.zl.serverStub.RpcProcessor;

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

    @Override
    public void listen() {

    }
}
