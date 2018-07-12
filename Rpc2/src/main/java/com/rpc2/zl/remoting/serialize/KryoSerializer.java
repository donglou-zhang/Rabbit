package com.rpc2.zl.remoting.serialize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;
import com.rpc2.zl.common.util.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.ref.SoftReference;

/**
 * Use the third-party serialization
 *
 * @author Vincent
 * Created  on 2017/11/12.
 */
public class KryoSerializer implements RpcSerializer {

    private static final KryoSerializer INSTANCE = new KryoSerializer();

    /**
     * Hungry Chinese singleton model, when init the class, it will be instantiated
     * And it won't be changed after instantiation, so it is thread safe
     * @return
     */
    public static KryoSerializer getInstance() {
        return INSTANCE;
    }

    private KryoSerializer() {}

    /**
     * SoftReference won't prevent the garbage collection and it holds a soft reference of object
     * It provides <code>get()</> method to get the strong reference of the object
     */
    private static final ThreadLocal<SoftReference<Kryo>> CACHE = new ThreadLocal<SoftReference<Kryo>>() {
        @Override
        protected SoftReference<Kryo> initialValue() {
            Kryo kryo = newKryo();
            return new SoftReference<Kryo>(kryo);
        }
    };

    private static Kryo newKryo() {
        Kryo kryo = new Kryo();
//        kryo.register(RpcBody.class);
//        kryo.register(RpcMethod.class);
        kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
        return kryo;
    }

    private Kryo kryo() {
        if(CACHE.get() == null) {
            throw new RuntimeException("ThreadLocal<SoftReference<Kryo>> CACHE is null");
        }
        Kryo kryo = CACHE.get().get();
        if(kryo == null) {
            kryo = newKryo();
            CACHE.set(new SoftReference<Kryo>(kryo));
        }
        return kryo;
    }

    @Override
    public byte[] serialize(Object obj){
        Assert.notNull(obj);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        kryo().writeObject(output, obj);
        //Don't forget to flush and close, otherwise it won't work
        output.flush();
        output.close();
        return baos.toByteArray();
    }

    @Override
    public Object deserialize(byte[] data){
        return deserialize(data, 0);
    }

    @Override
    public Object deserialize(byte[] data, int off){
        Assert.notNull(data);
        ByteArrayInputStream bais = new ByteArrayInputStream(data, off, data.length - off);
        Input input = new Input(bais);
        Object ret = null;
//        ret = kryo().readObject(input, RpcBody.class);
        input.close();
        return ret;
    }
}
