package com.rabbit.zl.common;

import com.rabbit.zl.common.serialization.KryoSerializer;
import com.rabbit.zl.common.serialization.RpcSerialization;
import com.rabbit.zl.rpc.protocol.model.RpcBody;
import com.rabbit.zl.rpc.registry.example.service.TestHello;
import org.junit.Test;

/**
 * Created by Vincent on 2017/12/6.
 */
public class TestSerializer {

    @Test
    public void testKryoSerializer() {
        RpcSerialization<RpcBody> serialization = KryoSerializer.getInstance();
        RpcBody rb = new RpcBody();
        rb.setRpcId("test001");
        rb.setRpcReturn("Hello, Vincent");
        rb.setRpcInterface(TestHello.class);
        byte[] data = serialization.serialize(rb);
        System.out.println(data.length);
        System.out.println(serialization.deserialize(serialization.serialize(rb)));
    }
}
