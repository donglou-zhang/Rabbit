package rpc.transmission;

import serverStub.RpcProcessor;

/**
 * @author Vincent
 * Created on 2017/11/13.
 */
public interface RpcAcceptor {

    /**
     * Close itself and release all the resource
     */
    void close();

    /**
     * Set the rpc processor
     * On the server side
     *
     * @param processor
     */
    void setProcessor(RpcProcessor processor);

    /**
     * Set the max number of accepted connections
     *
     * @param connections
     */
    void setConnections(int connections);

    /**
     * Set the server host and port
     *
     * @param host
     * @param port
     */
    void setAddress(String host, int port);
}
