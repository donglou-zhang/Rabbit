package com.rabbit.zl.rpc.registry.example.service;

import com.rabbit.zl.rpc.registry.RpcService;

@RpcService(value = TestHello.class)
public class TestHelloImpl implements TestHello {
    @Override
    public String Hello(String name) {
        return "Hello " + name;
    }
}
