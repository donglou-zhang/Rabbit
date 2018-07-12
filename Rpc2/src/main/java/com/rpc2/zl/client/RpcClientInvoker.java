package com.rpc2.zl.client;

import com.rpc2.zl.rpc.common.AbstractRpcInvoker;
import com.rpc2.zl.rpc.common.RpcInvocation;
import com.rpc2.zl.rpc.filter.RpcFilter;
import com.rpc2.zl.rpc.protocol.RpcMessage;
import com.rpc2.zl.rpc.protocol.RpcRequest;

import java.util.Map;

/**
 * Created by Vincent on 2018/7/11.
 */
public class RpcClientInvoker extends AbstractRpcInvoker<RpcMessage>{

    //InheritableThreadLocal适用于把数据从父线程传递到子线程；但为了保护线程安全性，应该只对不可变对象使用InheritableThreadLocal
    private final ThreadLocal<RpcRequest> rpcRequestThreadLocal = new InheritableThreadLocal<>();

    protected RpcClientInvoker(Map<String, Object> handlerMap, Map<String, RpcFilter> filterMap) {
        super(handlerMap, filterMap);
    }

    public Object invoke(RpcInvocation invocation) {
        RpcRequest request = this.getRpcRequest();

        return null;
    }

    public RpcRequest getRpcRequest() {
        return this.rpcRequestThreadLocal.get();
    }

    public void setRpcRequest(RpcRequest request) {
        this.rpcRequestThreadLocal.set(request);
    }
}
