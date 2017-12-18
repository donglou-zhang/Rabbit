package com.rabbit.zl.factory;

import com.rabbit.zl.client.RpcClient;
import com.rabbit.zl.rpc.registry.zookeeper.ServiceDiscovery;

/**
 * Test for producing {@link RpcClient} instance
 *
 * @author Vincent
 * Created  on 2017/12/04.
 */
public class ProduceRpcClient {

    public static RpcClient getRpcClient() {
        ServiceDiscovery discovery = new ServiceDiscovery("127.0.0.1", 2181, "Rabbit");
        RpcClient client = new RpcClient(discovery);
        return client;
    }
}
