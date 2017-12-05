package com.rabbit.zl.factory;

import com.rabbit.zl.client.RpcClient;

/**
 * Test for producing {@link RpcClient} instance
 *
 * @author Vincent
 * Created  on 2017/12/04.
 */
public class ProduceRpcClient {

    public static RpcClient getRpcClient() {
        RpcClient client = new RpcClient();
        return client;
    }
}
