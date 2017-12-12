package com.rabbit.zl.rpc.registry.example.service;

import com.rabbit.zl.rpc.registry.RpcService;

/**
 * Created by Vincent on 2017/12/8.
 */

@RpcService(value = TestOperator.class)
public class TestOperatorImpl implements TestOperator {

    @Override
    public int add(int a, int b) {
        return a+b;
    }
}
