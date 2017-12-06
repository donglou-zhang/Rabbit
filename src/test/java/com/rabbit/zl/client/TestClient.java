package com.rabbit.zl.client;

import com.rabbit.zl.common.test.CaseCounter;
import com.rabbit.zl.factory.ProduceRpcClient;
import com.rabbit.zl.rpc.registry.example.service.TestHello;
import org.junit.Test;

/**
 * @author Vincent
 * Created  on 2017/12/04.
 */
public class TestClient {

    @Test
    public void testGetBean() {
        RpcClient client = ProduceRpcClient.getRpcClient();
        TestHello service = client.getBean(TestHello.class);
        System.out.println("In testGetBean(), the result is: " + service.Hello("Vincent"));
        System.out.println(String.format("[Rabbit] (^_^) <%s> Test Case Success -> Client getBean. ", CaseCounter.incr(1)));
    }
}
