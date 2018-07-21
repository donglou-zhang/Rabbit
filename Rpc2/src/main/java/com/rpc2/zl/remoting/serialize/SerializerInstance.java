package com.rpc2.zl.remoting.serialize;

import com.rpc2.zl.common.util.PropertyUtil;

/**
 * Created by Vincent on 2018/7/19.
 */
public class SerializerInstance implements RpcSerializer{

    private String serializeType;

    private RpcSerializer serializer;

    private SerializerInstance() {
        this.serializeType = PropertyUtil.getProperty("serializer.type");
        if(this.serializeType.equals("jdk")) {
            serializer = new JdkSerializer();
        } else if(this.serializeType.equals("kryo")) {
            serializer = KryoSerializer.getInstance();
        }
    }

    @Override
    public byte[] serialize(Object obj) {
        return serializer.serialize(obj);
    }

    @Override
    public Object deserialize(byte[] data) {
        return serializer.deserialize(data);
    }

    @Override
    public Object deserialize(byte[] data, int off) {
        return serializer.deserialize(data, off);
    }

    private static class InnerSerializerInstance {
        private static final SerializerInstance INSTANCE = new SerializerInstance();
    }

    public static SerializerInstance getInstance() {
        return InnerSerializerInstance.INSTANCE;
    }
}
