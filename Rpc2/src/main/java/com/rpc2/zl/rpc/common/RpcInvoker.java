package com.rpc2.zl.rpc.common;

/**
 * Created by Vincent on 2018/7/11.
 */
public interface RpcInvoker {

    Object invoke(RpcInvocation invocation);
}
