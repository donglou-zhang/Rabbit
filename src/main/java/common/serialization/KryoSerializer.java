package common.serialization;

import common.exception.ProtocolException;

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
        return new byte[0];
    }

    @Override
    public Object deserialize(byte[] data) throws ProtocolException {
        return null;
    }

    @Override
    public Object deserialize(byte[] data, int off) throws ProtocolException {
        return null;
    }
}
