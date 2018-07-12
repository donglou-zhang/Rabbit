package com.rpc2.zl.rpc.proxy;

import com.google.common.collect.Maps;
import com.rpc2.zl.rpc.protocol.RpcResponseFuture;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Created by Vincent on 2018/7/12.
 */
public class RpcContext {

    @Getter @Setter
    private RpcResponseFuture responseFuture;

    @Getter @Setter
    private Map<String, Object> contextParams;

    /**
     * 使用ThreadLocal来保存context信息，保证了每次获取的是上次保存的. 定义为final类型，意味着rpcContextThreadLocal
     * 不可重定向（即不能重新指向一个新的ThreadLocal），但内部的Map是可以变化的
     */
    private static final ThreadLocal<RpcContext> rpcContextThreadLocal = new ThreadLocal<RpcContext>() {
        @Override
        protected RpcContext initialValue() {
            RpcContext context = new RpcContext();
            context.setContextParams(Maps.newHashMap());
            return context;
        }
    };

    public RpcContext() {}

    public Object getContextParam(String key) {
        return this.getContextParams().get(key);
    }

    public void addContextParam(String key, Object val) {
        this.contextParams.put(key, val);
    }

    public static RpcContext getRpcContext() {
        return rpcContextThreadLocal.get();
    }

    public static void removeRpcContext() {
        rpcContextThreadLocal.remove();
    }
}
