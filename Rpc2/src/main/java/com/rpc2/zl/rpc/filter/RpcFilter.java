package com.rpc2.zl.rpc.filter;

import com.rpc2.zl.rpc.common.RpcInvocation;
import com.rpc2.zl.rpc.common.RpcInvoker;

/**
 * Created by Vincent on 2018/7/11.
 */
public interface RpcFilter<T> {

    <T> T invoke(RpcInvoker invoker, RpcInvocation invocation);
}
