package com.rabbit.zl.factory;

import com.rabbit.zl.rpc.registry.RpcRegistryService;

/**
 * Test for producing {@link RpcRegistryService} instance
 *
 * @author Vincent
 * Created  on 2017/11/22.
 */
public class ProduceRpcRegistryService {

    public static RpcRegistryService getRpcRegistryService() {
        RpcRegistryService service = new RpcRegistryService();
        service.setApplication("Rabbit");
        service.setRpcInterface("com.rabbit.zl.rpc.registry.example.service.TestHello");
        service.setMethodName("Hello");
        service.setVersion("1.0.0");
        service.setWeight(3);
        service.setServerHost("127.0.0.1");
        service.setServerPort(8001);
        return service;
    }
}
