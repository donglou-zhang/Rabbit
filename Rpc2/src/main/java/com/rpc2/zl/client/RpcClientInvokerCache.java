package com.rpc2.zl.client;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Vincent on 2018/7/16.
 */
public class RpcClientInvokerCache {

    //CopyOnWriteArrayList是一种读写分离思想，当写入时，先复制一份新的容器，然后往新的里添加，最后将引用指向新的容器
    private static CopyOnWriteArrayList<RpcClientInvoker> connectedHandlers = new CopyOnWriteArrayList<>();

    public static CopyOnWriteArrayList<RpcClientInvoker> getConnectedHandlers() {
        return connectedHandlers;
    }

    public static CopyOnWriteArrayList<RpcClientInvoker> getConnectedHandlersClone() {
        return (CopyOnWriteArrayList<RpcClientInvoker>) RpcClientInvokerCache.getConnectedHandlers().clone();
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
