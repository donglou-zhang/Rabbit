package com.rpc2.zl.common.threadPool;

import java.util.concurrent.Executor;

/**
 * Created by Vincent on 2018/7/17.
 */
public interface ThreadPool {

    Executor getExecutor(int threadSize, int queues);
}
