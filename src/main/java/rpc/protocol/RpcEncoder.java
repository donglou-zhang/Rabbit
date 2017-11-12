package rpc.protocol;

import common.exception.ProtocolException;
import common.serialization.RpcSerialization;
import common.serialization.SerializerRegistry;
import common.util.Assert;
import rpc.protocol.model.RpcBody;
import rpc.protocol.model.RpcHeader;
import rpc.protocol.model.RpcMessage;

/**
 * @author Vincent
 * Created  on 2017/11/12.
 */
public class RpcEncoder {

    private SerializerRegistry serializerRegistry = SerializerRegistry.getInstance();

    public byte[] encode(RpcMessage rm) throws ProtocolException {
        if(rm == null)
            return null;
        RpcHeader rh = rm.getHeader();
        RpcBody rb = rm.getBody();

        Assert.notNull(rh);
        Assert.notNull(rb);

        //get the specified serialization
        RpcSerialization serializer = serializerRegistry.findSerializerByType(rh.getSt());

        return null;
    }
}
