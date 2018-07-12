package com.rpc2.zl.rpc.common;

import com.google.common.collect.Lists;
import com.rpc2.zl.rpc.protocol.RpcRequest;
import com.rpc2.zl.rpc.filter.RpcFilter;

import java.util.List;
import java.util.Map;

/**
 * Created by Vincent on 2018/7/11.
 */
public abstract class AbstractRpcInvoker<T> implements RpcInvoker{

    private final Map<String, Object> handlerMap;

    private final Map<String, RpcFilter> filterMap;

    protected AbstractRpcInvoker(Map<String, Object> handlerMap, Map<String, RpcFilter> filterMap) {
        this.handlerMap = handlerMap;
        this.filterMap = filterMap;
    }

    public RpcInvocation buildRpcInvocation(final RpcRequest request) {
        RpcInvocation invocation = new RpcInvocation() {
            public String getRequestId() {
                return request.getRequestId();
            }

            public String getClassName() {
                return request.getClassName();
            }

            public String getMethodName() {
                return request.getMethodName();
            }

            public Object[] getParameters() {
                return request.getParameters();
            }

            public Class<?>[] getParameterTypes() {
                return request.getParameterTypes();
            }

            public int getMaxExecutesCount() {
                return request.getMaxExecutesCount();
            }

            public Map<String, Object> getContextParameters() {
                return request.getContextParameters();
            }
        };
        return invocation;
    }

    public RpcInvoker buildRpcInvokerChain(final RpcInvoker invoker) {
        RpcInvoker last = invoker;
        List<RpcFilter> filters = Lists.newArrayList(this.filterMap.values());
        //TODO
        return last;
    }

    public abstract Object invoke(RpcInvocation invocation);
}
