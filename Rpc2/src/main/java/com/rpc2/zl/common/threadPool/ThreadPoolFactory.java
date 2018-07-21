package com.rpc2.zl.common.threadPool;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by Vincent on 2018/7/17.
 */
public class ThreadPoolFactory {

    @Autowired
    private Map<String, ThreadPool> threadPoolMap;

    public ThreadPool getThreadPool(String threadPoolName) {
        return this.threadPoolMap.get(threadPoolName);
    }
}
