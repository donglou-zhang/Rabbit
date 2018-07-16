package com.rpc2.zl.rpc.common.loadbalance;

import java.util.Random;

/**
 * Created by Vincent on 2018/7/16.
 */
public class RandomLoadBalance implements LoadBalanceService {
    /**
     * 随机返回1~size之间的任意整数
     * @param size
     * @return
     */
    @Override
    public int index(int size) {
        return new Random().nextInt(size) + 1;
    }
}
