package com.rabbit.zl.common.serialization;


import java.util.HashMap;
import java.util.Map;

/**
 * provides different kinds of serialization registry
 *
 * @author Vincent
 * Created  on 2017/11/12.
 */
public class SerializerRegistry {

    private Map<Byte, RpcSerialization> registry = new HashMap<>();

    private final static SerializerRegistry INSTANCE = new SerializerRegistry();

    public static SerializerRegistry getInstance() {
        return INSTANCE;
    }

    private SerializerRegistry() {}

    public RpcSerialization findSerializerByType(byte type) {
        return registry.get(type);
    }

    public void register(byte type, RpcSerialization serialization) {
        if(registry.containsKey(type)) {
            throw new IllegalArgumentException("Serialization 'type' has been existed!");
        }
        registry.put(type, serialization);
    }

    public void unregister(byte type) {
        registry.remove(type);
    }

}
