package com.rabbit.zl.rpc.invoke;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbit.zl.rpc.protocol.model.RpcMessage;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RpcInvocationHandler implements InvocationHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcInvocationHandler.class);

    @Getter @Setter private RpcInvoker rpcInvoker;

    public RpcInvocationHandler(RpcInvoker invoker) {
        this.rpcInvoker = invoker;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> rpcInterface = method.getDeclaringClass();
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        RpcMessage request = RpcMessage.newRequestMessage(rpcInterface, methodName, parameterTypes, args);

        LOGGER.debug("[Rabbit] Rpc client proxy before invocation, | request = {}", request);
        RpcMessage response = rpcInvoker.invoke(request);
        LOGGER.debug("[Rabbit] Rpc client proxy before invocation, | request = {}", response);

        return RpcMessage.unpackResponseMessage(response);

    }
}
