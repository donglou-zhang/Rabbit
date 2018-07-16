package com.rpc2.zl.client;

import com.rpc2.zl.remoting.transport.RpcConnector;
import com.rpc2.zl.rpc.common.AbstractRpcInvoker;
import com.rpc2.zl.rpc.common.RpcInvocation;
import com.rpc2.zl.rpc.filter.RpcFilter;
import com.rpc2.zl.rpc.protocol.RpcMessage;
import com.rpc2.zl.rpc.protocol.RpcRequest;
import com.rpc2.zl.rpc.protocol.RpcResponseFuture;

import java.util.Map;

/**
 * Created by Vincent on 2018/7/11.
 */
public class RpcClientInvoker extends AbstractRpcInvoker<RpcMessage>{

    //InheritableThreadLocal适用于把数据从父线程传递到子线程；但为了保护线程安全性，应该只对不可变对象使用InheritableThreadLocal
    private final ThreadLocal<RpcRequest> rpcRequestThreadLocal = new InheritableThreadLocal<>();

    private RpcConnector connector;

    public RpcClientInvoker(Map<String, RpcFilter> filterMap) {
        super(null, filterMap);
    }

    protected RpcClientInvoker(Map<String, Object> handlerMap, Map<String, RpcFilter> filterMap) {
        super(handlerMap, filterMap);
    }

    @Override
    public RpcResponseFuture invoke(RpcInvocation invocation) {
        RpcRequest request = this.getRpcRequest();
        RpcResponseFuture responseFuture = connector.send(request);
        return responseFuture;
    }

    public RpcRequest getRpcRequest() {
        return this.rpcRequestThreadLocal.get();
    }

    public void setRpcRequest(RpcRequest request) {
        this.rpcRequestThreadLocal.set(request);
    }

    public void close() {

    }
}
