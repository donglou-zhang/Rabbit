package com.rpc2.zl.server;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.rpc2.zl.rpc.common.AbstractRpcInvoker;
import com.rpc2.zl.rpc.common.RpcInvocation;
import com.rpc2.zl.rpc.exception.RpcException;
import com.rpc2.zl.rpc.filter.RpcFilter;
import com.rpc2.zl.rpc.protocol.RpcMessage;
import com.rpc2.zl.rpc.protocol.RpcResponse;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Created by Vincent on 2018/7/18.
 */
public class RpcServerInvoker extends AbstractRpcInvoker {

    // 存储service与serviceBean的映射
    private Map<String, Object> handlerMap;

    public RpcServerInvoker() {}

    public RpcServerInvoker(Map<String, Object> handlerMap, Map<String, RpcFilter> filterMap) {
        super(handlerMap, filterMap);
        this.handlerMap = handlerMap;
    }

    @Override
    public Object invoke(RpcInvocation invocation) {
        String className = invocation.getClassName();
        Object serviceBean = handlerMap.get(className);

        Class<?> serviceClass = serviceBean.getClass();
        String methodName = invocation.getMethodName();
        Class<?>[] parameterTypes = invocation.getParameterTypes();
        Object[] parameters = invocation.getParameters();

        MethodAccess methodAccess = MethodAccess.get(serviceClass);
        int methodIndex = methodAccess.getIndex(methodName, parameterTypes);
        Object ret = methodAccess.invoke(serviceBean, methodIndex, parameters);
        RpcResponse response = new RpcResponse();
        response.setResult(ret);
        response.setRequestId(invocation.getRequestId());
        return response;
    }
}
