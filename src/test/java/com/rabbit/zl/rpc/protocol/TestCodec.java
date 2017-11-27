package com.rabbit.zl.rpc.protocol;

import com.rabbit.zl.common.serialization.KryoSerializer;
import com.rabbit.zl.common.serialization.SerializerRegistry;
import com.rabbit.zl.common.test.CaseCounter;
import com.rabbit.zl.rpc.protocol.codec.DefaultRpcDecoder;
import com.rabbit.zl.rpc.protocol.codec.DefaultRpcEncoder;
import com.rabbit.zl.rpc.protocol.model.RpcMessage;
import com.rabbit.zl.factory.ProduceRpcMessage;
import org.junit.Test;

import java.util.List;

/**
 * Test for rpcMessage encoder and decoder
 *
 * @author Vincent
 * Created  on 2017/11/24.
 */
public class TestCodec {

    /**
     * Test default encoder and decoder. Through compare the original rpcMessage and
     * the rpcMessage which is decode from the bytes of original rpcMessage
     */
    @Test
    public void testCodec() {
        testSerializerRegistry();

        RpcMessage message = ProduceRpcMessage.getRpcMessage();
        System.out.println("Before encode, the message is: " + message.toString());
        List<RpcMessage> rst = new DefaultRpcDecoder().decode(new DefaultRpcEncoder().encode(message));
        System.out.println("get " + rst.size() + " RpcMessage(s)");

        for(int i = 0; i < rst.size(); i++) {
            System.out.println("After decode, the (" + (i+1) + ") message is: " + rst.get(i).toString());
        }
        System.out.println(String.format("[Rabbit] (^_^) <%s> Test Case Success -> default encoder and decoder. ", CaseCounter.incr(2)));
    }

    /**
     * When encode and decode, it needs serializer.
     * All kinds of Serializer should be registered first.
     */
    @Test
    public void testSerializerRegistry() {
        SerializerRegistry serializerRegistry = SerializerRegistry.getInstance();
        serializerRegistry.register((byte)1, KryoSerializer.getInstance());
        System.out.println(String.format("[Rabbit] (^_^) <%s> Test Case Success -> Serializer registry success. ", CaseCounter.incr(1)));
    }
}
