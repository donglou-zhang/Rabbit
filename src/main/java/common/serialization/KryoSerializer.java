package common.serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;
import common.exception.ProtocolException;
import common.util.Assert;
import rpc.protocol.model.RpcBody;
import rpc.protocol.model.RpcHeader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.ref.SoftReference;

/**
 * Use the third-party serialization
 *
 * @author Vincent
 * Created  on 2017/11/12.
 */
public class KryoSerializer implements RpcSerialization {

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
            return super.initialValue();
        }
    };

    private static Kryo newKryo() {
        Kryo kryo = new Kryo();
//        kryo.register(RpcHeader.class);
        kryo.register(RpcBody.class);
        kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
        return kryo;
    }

    private Kryo kryo() {
        Kryo kryo = CACHE.get().get();
        if(kryo == null) {
            kryo = newKryo();
            CACHE.set(new SoftReference<Kryo>(kryo));
        }
        return kryo;
    }

    /**
     * kryo serializer is the preferred serialization
     *
     * @return
     */
    @Override
    public byte type() {
        return 1;
    }

    @Override
    public byte[] serialize(Object obj) throws ProtocolException {
        Assert.notNull(obj);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        kryo().writeObject(output, obj);
        return baos.toByteArray();
    }

    @Override
    public Object deserialize(byte[] data) throws ProtocolException {
        return deserialize(data, 0);
    }

    @Override
    public Object deserialize(byte[] data, int off) throws ProtocolException {
        Assert.notNull(data);
        ByteArrayInputStream bais = new ByteArrayInputStream(data, off, data.length - off);
        Input input = new Input(bais);
        RpcBody rpcBody = kryo().readObject(input, RpcBody.class);
        input.close();
        return rpcBody;
    }
}
