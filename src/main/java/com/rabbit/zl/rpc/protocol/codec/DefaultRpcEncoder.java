package com.rabbit.zl.rpc.protocol.codec;

import com.rabbit.zl.common.exception.ProtocolException;
import com.rabbit.zl.common.serialization.RpcSerialization;
import com.rabbit.zl.common.serialization.SerializerRegistry;
import com.rabbit.zl.common.util.Assert;
import com.rabbit.zl.common.util.ByteUtil;
import com.rabbit.zl.rpc.protocol.model.RpcBody;
import com.rabbit.zl.rpc.protocol.model.RpcHeader;
import com.rabbit.zl.rpc.protocol.model.RpcMessage;
import com.rabbit.zl.rpc.protocol.model.RpcOption;

import java.util.HashMap;
import java.util.Map;

/**
 * Default rpc encoder, encodes a {@code RpcMessage} object into bytes
 * thread safe
 *
 * @author Vincent
 * Created  on 2017/11/12.
 */
public class DefaultRpcEncoder {

    private static SerializerRegistry serializerRegistry = SerializerRegistry.getInstance();

    public DefaultRpcEncoder() {

    }

    public byte[] encode(RpcMessage rm) throws ProtocolException {
        if(rm == null)
            return null;
        RpcHeader rh = rm.getHeader();
        RpcBody rb = rm.getBody();

        Assert.notNull(rh);
        Assert.notNull(rb);

        //get the specified serialization
        RpcSerialization serializer = serializerRegistry.findSerializerByType(rh.getSt());

        if(serializer == null) {
            throw new ProtocolException("Can not find the specific serializer: st = " + rh.getSt());
        }

        byte[] body = encodeBody(rb, serializer);

        rh.setBodySize(body.length);
        byte[] message = new byte[rh.getHeaderSize() + body.length];
        encodeHeader(message, rh);
        // copy body array to message array
        System.arraycopy(body, 0, message, rh.getHeaderSize(), body.length);

        return message;
    }

    private void encodeHeader(byte[] b, RpcHeader header) {
        ByteUtil.shortToBytes(header.getMagic(), b, 0);
        ByteUtil.shortToBytes(header.getHeaderSize(), b, 2);
        b[4] = header.getVersion();
        // st + hb + ow + rp = 1 byte
        b[5] = (byte)(header.getSt() | header.getHb() | header.getOw() | header.getRp());
        b[6] = header.getStatusCode();
        // yet not set 'reserved'
        ByteUtil.longToBytes(header.getMid(), b, 8);
        ByteUtil.intToBytes(header.getBodySize(), b, 16);
    }

    private byte[] encodeBody(RpcBody body, RpcSerialization serialization) {
        return serialization.serialize(body);
    }
}
