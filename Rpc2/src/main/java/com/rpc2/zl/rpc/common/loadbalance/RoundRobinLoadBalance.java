package com.rpc2.zl.rpc.common.loadbalance;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Vincent on 2018/7/16.
 */
public class RoundRobinLoadBalance implements LoadBalanceService {

    private AtomicInteger roundRobin = new AtomicInteger(0);

    private static final int MAX_VALUE = 1000;

    private static final int MIN_VALUE = 1;

    private AtomicInteger getRoundRobinValue() {
        if(this.roundRobin.getAndAdd(1) > MAX_VALUE) {
            this.roundRobin.set(MIN_VALUE);
        }
        return this.roundRobin;
    }

    @Override
    public int index(int size) {
        return ((this.getRoundRobinValue().get() + size) % size);
    }
}
