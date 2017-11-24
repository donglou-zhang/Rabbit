package com.rabbit.zl.rpc.registry.example.service;

import com.rabbit.zl.rpc.registry.RpcService;

@RpcService(value = HelloService.class)
public class HelloServiceImpl implements HelloService{
    @Override
    public String Hello(String name) {
        return "Hello " + name;
    }
}
