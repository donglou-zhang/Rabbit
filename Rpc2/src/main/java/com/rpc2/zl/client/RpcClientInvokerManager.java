package com.rpc2.zl.client;

import com.rpc2.zl.common.constant.Constants;
import com.rpc2.zl.remoting.transport.RpcConnector;
import com.rpc2.zl.remoting.transport.netty.NettyRpcConnector;
import com.rpc2.zl.rpc.config.ClientConfig;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Vincent on 2018/7/16.
 */
public class RpcClientInvokerManager {

    private static final ReentrantLock lock = new ReentrantLock();

    private static final Condition connected = lock.newCondition();

    private static ClientConfig clientConfig;

    private static RpcConnector connector = new NettyRpcConnector();

    // ScheduledExecutorService将定时任务和线程池结合使用
    private static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    /**
     * 单例模式，饿汉式（无lazy loading），类加载时就完成实例化
     */
    private final static RpcClientInvokerManager instance = new RpcClientInvokerManager();

    static {
        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    List<RpcClientInvoker> notConnectedHandlers = RpcClientInvokerCache.getNotConnectedHandlers();
                    if(!CollectionUtils.isEmpty(notConnectedHandlers)) {
                        for(RpcClientInvoker invoker : notConnectedHandlers) {
                            connector.connect(clientConfig);
                        }
                        RpcClientInvokerCache.clearNotConnectedHandler();
                    }
                }
            }
        }, Constants.RECONNECT_TIME_SECONDS, TimeUnit.SECONDS);
    }

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
                connector.closeAll();
            }
        }, "RpcShutdownHook-RpcClientInvoker"));
    }

    public static RpcClientInvokerManager getInstance(ClientConfig config) {
        clientConfig = config;
        return instance;
    }

    public RpcClientInvoker getClientInvoker() {
        return null;
    }

    public static void addConnectedHandler(RpcClientInvoker invoker) {
        RpcClientInvokerCache.addConnectedHandler(invoker);
        signalAvailableHandler();
    }

    private static void signalAvailableHandler() {
        lock.lock();
        try {
            connected.signalAll();
        } finally {
            lock.unlock();
        }
    }

    private boolean waitForHandler() throws InterruptedException {
        lock.lock();
        try {
            return connected.await(clientConfig.getConnectTimeoutMillis(), TimeUnit.MILLISECONDS);
        } finally {
            lock.unlock();
        }
    }
}
