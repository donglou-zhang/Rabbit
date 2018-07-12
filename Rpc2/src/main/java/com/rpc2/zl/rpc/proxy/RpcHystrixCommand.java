package com.rpc2.zl.rpc.proxy;

import com.netflix.hystrix.*;
import com.rpc2.zl.client.RpcReference;
import com.rpc2.zl.rpc.protocol.RpcRequest;
import com.rpc2.zl.rpc.config.ClientConfig;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by Vincent on 2018/7/11.
 */
public class RpcHystrixCommand extends HystrixCommand{

    private Method method;

    private Object[] params;

    private RpcReference reference;

    private ClientConfig clientConfig;

    protected RpcHystrixCommand(Setter setter) {
        super(setter);
    }

    public RpcHystrixCommand(Method method, Object[] params, RpcReference reference, ClientConfig clientConfig) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("CircuitBreakerRpcHystrixCommandGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("CircuitBreakerRpcHystrixCommandKey"))
                .andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter()
                        .withCircuitBreakerEnabled(true)
                        .withCircuitBreakerRequestVolumeThreshold(1)
                        .withCircuitBreakerErrorThresholdPercentage(50)
                        .withCircuitBreakerSleepWindowInMilliseconds(5*1000)
                        .withMetricsRollingStatisticalWindowInMilliseconds(10*1000)
                )
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("CircuitBreakerRpcHystrixCommandPool"))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(100)));

        this.method = method;
        this.params = params;
        this.reference = reference;
        this.clientConfig = clientConfig;
    }

    protected Object run() throws Exception {
        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameters(this.params);
        request.setParameterTypes(method.getParameterTypes());

        if(this.reference != null) {
            request.setMaxExecutesCount(this.reference.cliMaxExecuteCount());
        }

        request.setContextParameters(RpcContext.getRpcContext().getContextParams());



        return null;
    }
}
