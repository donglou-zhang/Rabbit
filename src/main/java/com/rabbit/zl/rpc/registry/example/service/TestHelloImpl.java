package com.rabbit.zl.rpc.registry.example.service;

import com.rabbit.zl.rpc.registry.RpcService;

@RpcService(value = TestHello.class)
public class TestHelloImpl implements TestHello {
    @Override
    public String hello(String name) {
        return "Hello " + name;
    }

    @Override
    public String time() {
        return "当前时间为：" + String.valueOf(System.currentTimeMillis());
    }
}
