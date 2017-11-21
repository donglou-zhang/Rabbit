package rpc.executor;

import rpc.monitor.MonitoringThreadPoolExecutor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author Vincent
 * Created  on 2017/11/21.
 */
public class RpcThreadPoolExecutor extends MonitoringThreadPoolExecutor{

    public RpcThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    @Override
    public String toString() {
        return String.format("RpcThreadPoolExecutor( " +
        "corePoolSize = %s, " +
        "maximumPoolSize = %s, " +
        "keepAliveTime = %s, " +
        "workQueueSize = %s, " +
        "poolSize = %s, " +
        "activeCount = %s, " +
        "largestPoolSize = %s, " +
        "taskCount = %s, " +
        "completedTaskCount = %s",
                getCorePoolSize(),
                getMaximumPoolSize(),
                getKeepAliveTime(TimeUnit.SECONDS),
                getQueue().size(),
                getPoolSize(),
                getActiveCount(),
                getLargestPoolSize(),
                getTaskCount(),
                getCompletedTaskCount());
    }
}
