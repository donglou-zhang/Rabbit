package com.rpc2.zl.rpc.exception;

/**
 * Created by Vincent on 2018/7/11.
 */
public class RpcException extends RuntimeException{

    public RpcException() {
        super("Rpc Exception");
    }

    public RpcException(Exception e) {
        super(e);
    }

    public RpcException(String msg) {
        super(msg);
    }
}
