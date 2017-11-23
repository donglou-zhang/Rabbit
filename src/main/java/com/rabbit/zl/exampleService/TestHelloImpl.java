package com.rabbit.zl.exampleService;

import com.rabbit.zl.rpc.registry.RpcService;

/**
 * Created by Vincent on 2017/11/23.
 */
@RpcService(TestHello.class)
public class TestHelloImpl implements TestHello {
    @Override
    public String testHello(String call) {
        return "This is return: " + call;
    }
}
