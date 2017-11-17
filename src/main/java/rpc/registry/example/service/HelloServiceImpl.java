package rpc.registry.example.service;

import rpc.registry.RpcService;

@RpcService(HelloService.class)
public class HelloServiceImpl implements HelloService{
    @Override
    public String Hello(String name) {
        return "Hello " + name;
    }
}
