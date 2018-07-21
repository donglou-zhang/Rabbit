package com.rpc2.zl.client;

import lombok.Getter;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Vincent on 2018/7/16.
 */
public class RpcClientInvokerCache {

    //CopyOnWriteArrayList是一种读写分离思想，当写入时，先复制一份新的容器，然后往新的里添加，最后将引用指向新的容器
    @Getter
    private static CopyOnWriteArrayList<RpcClientInvoker> connectedHandlers = new CopyOnWriteArrayList<>();

    @Getter
    private static CopyOnWriteArrayList<RpcClientInvoker> notConnectedHandlers = new CopyOnWriteArrayList<>();

    public static void addConnectedHandler(RpcClientInvoker invoker) {
        CopyOnWriteArrayList<RpcClientInvoker> connectedHandlersClone = getConnectedHandlersClone();
        connectedHandlersClone.add(invoker);
        connectedHandlers = connectedHandlersClone;
    }

    public static CopyOnWriteArrayList<RpcClientInvoker> getConnectedHandlersClone() {
        return (CopyOnWriteArrayList<RpcClientInvoker>) RpcClientInvokerCache.getConnectedHandlers().clone();
    }

    public static CopyOnWriteArrayList<RpcClientInvoker> getNotConnectedHandlersClone() {
        return (CopyOnWriteArrayList<RpcClientInvoker>) RpcClientInvokerCache.getNotConnectedHandlers().clone();
    }

    public static void clearNotConnectedHandler() {
        CopyOnWriteArrayList<RpcClientInvoker> notConnectedHandlersClone = getNotConnectedHandlersClone();
        notConnectedHandlersClone.clear();
        notConnectedHandlers = notConnectedHandlersClone;
    }

    public static RpcClientInvoker get(int i) {
        return connectedHandlers.get(i);
    }

    public static void clear() {
        CopyOnWriteArrayList<RpcClientInvoker> newHandlers = getConnectedHandlersClone();
        newHandlers.clear();
        connectedHandlers = newHandlers;
    }

    public static int getSize() {
        return connectedHandlers.size();
    }
}
