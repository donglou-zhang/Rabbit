package com.rpc2.zl.client;

import com.rpc2.zl.rpc.config.ClientConfig;
import io.netty.channel.EventLoopGroup;

/**
 * Created by Vincent on 2018/7/16.
 */
public class RpcClientInvokerManager {

    private static ClientConfig clientConfig;

    /**
     * 单例模式，饿汉式（无lazy loading），类加载时就完成实例化
     */
    private final static RpcClientInvokerManager instance = new RpcClientInvokerManager();

    private RpcClientInvokerManager() {
        // 每个Java程序都会有一个Runtime实例，该类会被自动创建
        // addShutdownHook是关闭虚拟机时触发的动作(比如可以用于清理资源之类的)
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0, len=RpcClientInvokerCache.getSize(); i<len; i++) {
                    RpcClientInvoker connectedServerHandler = RpcClientInvokerCache.get(i);
                    connectedServerHandler.close();
                }

            }
        }));
    }

    public static RpcClientInvokerManager getInstance(ClientConfig config) {
        clientConfig = config;
        return instance;
    }
}
