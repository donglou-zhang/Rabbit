package com.rabbit.zl.factory;

import com.rabbit.zl.rpc.protocol.model.*;
import com.rabbit.zl.rpc.registry.example.service.TestHelloService;

import java.net.InetSocketAddress;

/**
 * Test for producing {@link RpcMessage} instance
 *
 * @author Vincent
 * Created  on 2017/11/24.
 */
public class ProduceRpcMessage {

    public static RpcMessage getRpcMessage() {
        RpcMessage message = new RpcMessage();
        RpcHeader header = new RpcHeader();
        RpcBody body = new RpcBody();

        header.setMid(3L);
        body.setRpcId("001");
        body.setApplication("Rabbit");
        body.setRpcInterface(TestHelloService.class);
        RpcMethod method = new RpcMethod();
        method.setName("hello");
        method.setParameterTypes(String.class);
        method.setParameters("vincent");
        body.setRpcMethod(method);
        RpcOption option = new RpcOption();
        option.setClientAddress(new InetSocketAddress("127.0.0.1", 8000));
        option.setServerAddress(new InetSocketAddress("127.0.0.1", 8001));
        option.setRpcTimeoutInMillis(4960);
        body.setRpcOption(option);

        message.setHeader(header);
        message.setBody(body);

        return message;
    }
}
