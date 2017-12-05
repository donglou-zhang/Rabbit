package com.rabbit.zl.rpc.transmission;

import com.rabbit.zl.serverStub.RpcProcessor;
import com.rabbit.zl.transfer.netty.NettyServerAcceptor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Vincent
 * Created on 2017/11/17.
 */
public class DefaultRpcAcceptor implements RpcAcceptor{

    private RpcAcceptor acceptor;

    @Getter private RpcProcessor processor;

    private String serverHost;

    private int serverPort;

    public DefaultRpcAcceptor() {}

    public DefaultRpcAcceptor(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        init();
    }

    public void init() {
        acceptor = new NettyServerAcceptor(this.serverHost, this.serverPort);
    }

    @Override
    public void close() {

    }

    @Override
    public void setConnections(int connections) {

    }

    @Override
    public void setAddress(String host, int port) {

    }

    @Override
    public void setProcessor(RpcProcessor processor) {
        acceptor.setProcessor(processor);
    }

    @Override
    public void listen() {
        acceptor.listen();
    }
}
