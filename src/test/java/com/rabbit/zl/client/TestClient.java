package com.rabbit.zl.client;

import com.rabbit.zl.common.test.CaseCounter;
import com.rabbit.zl.factory.ProduceRpcClient;
import com.rabbit.zl.rpc.registry.example.service.TestHello;
import com.rabbit.zl.rpc.registry.example.service.TestOperator;
import org.junit.Test;

/**
 * Test for {@link RpcClient}
 * Through invoking getBean(), test the framework is working correctly or not
 *
 * @author Vincent
 * Created  on 2017/12/04.
 */
public class TestClient {

    @Test
    public void testGetBean() {
        RpcClient client = ProduceRpcClient.getRpcClient();
        TestHello service = client.getBean(TestHello.class);
        System.out.println("In testGetBean(), the result of hello() is: " + service.hello("Vincent"));
        System.out.println("In testGetBean(), the result of time() is: " + service.time());
        TestOperator service2 = client.getBean(TestOperator.class);
        System.out.println("In testGetBean(), the result of add() is: " + service2.add(10,25));
        System.out.println(String.format("[Rabbit] (^_^) <%s> Test Case Success -> Client getBean. ", CaseCounter.incr(3)));
    }
}
