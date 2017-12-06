package com.rabbit.zl.rpc.protocol.codec;

import com.rabbit.zl.common.exception.ProtocolException;
import com.rabbit.zl.common.serialization.RpcSerialization;
import com.rabbit.zl.common.serialization.SerializerRegistry;
import com.rabbit.zl.common.util.ByteUtil;
import com.rabbit.zl.rpc.protocol.model.RpcBody;
import lombok.Getter;
import com.rabbit.zl.rpc.protocol.model.RpcHeader;
import com.rabbit.zl.rpc.protocol.model.RpcMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Default rpc decoder decodes bytes to {@code RpcMessage} object
 * not thread safe
 *
 * @author Vincent
 * Created  on 2017/11/14.
 */
public class DefaultRpcDecoder extends AbstractProtocolDecoder{

    private SerializerRegistry serializerRegistry = SerializerRegistry.getInstance();

    private static final int MAGIC = 11;
    private static final int HEADER_SIZE = 12;
    private static final int VERSION = 13;
    // handle st | hb | ow | rp
    private static final int BIT_FLAG = 14;
    private static final int STATUS_CODE = 15;
    private static final int RESERVED = 16;
    private static final int MESSAGE_ID = 17;
    private static final int BODY_SIZE = 18;
    private static final int BODY = 20;

    @Getter private RpcMessage rpcMessage;

    public DefaultRpcDecoder() {}

    public List<RpcMessage> decode(byte[] bytes) throws ProtocolException {
        List<RpcMessage> messages = new ArrayList<>();
        adaptBuffer();
        buff.append(bytes);

        while(searchIndex < buff.length() || state == END) {
            switch (state) {
                case START:
                    handleStartState();
                    break;
                case MAGIC:
                    handleMagicState();
                    break;
                case HEADER_SIZE:
                    handleHeaderSizeState();
                    break;
                case VERSION:
                    handleVersionState();
                    break;
                case BIT_FLAG:
                    handleBitFlagState();
                    break;
                case STATUS_CODE:
                    handleStatusCodeState();
                    break;
                case RESERVED:
                    handleReservedState();
                    break;
                case MESSAGE_ID:
                    handleMessageIdState();
                    break;
                case BODY_SIZE:
                    handleBodySizeState();
                    break;
                case BODY:
                    handleBodyState();
                    break;
                case END:
                    handleEndState(messages);
                    break;
                default:
                    throw new IllegalStateException("Invalid decoder state!");
            }
        }

        return messages;
    }

    private void handleStartState() {
        if(buff.length() > splitIndex) {
            state = MAGIC;
        }
    }

    private void handleMagicState() {
        // It indicates that the buffer needs to read more bytes to cover the magic.
        // Cause the magic has two bytes.
        if(buff.length() < splitIndex + 2) {
            searchIndex = buff.length();
            return;
        }

        // To make sure the splitIndex if the beginning of a RpcMessage.
        if(RpcHeader.MAGIC_BIT_0 == buff.byteAt(splitIndex) && RpcHeader.MAGIC_BIT_1 == buff.byteAt(splitIndex + 1)) {
            rpcMessage = new RpcMessage();
            RpcHeader rh = new RpcHeader();
            rpcMessage.setHeader(rh);
            // Next, it needs to handle header_size,so move the searchIndex by 2 bytes from splitIndex(beginning).
            state = HEADER_SIZE;
            searchIndex = splitIndex + 2;
        }
    }

    private void handleHeaderSizeState() {
        if(buff.length() < splitIndex + 4) {
            searchIndex = buff.length();
            return;
        }

        short headerSize = ByteUtil.bytesToShort(buff.buffer(), splitIndex + 2);
        rpcMessage.getHeader().setHeaderSize(headerSize);
        state = VERSION;
        searchIndex = splitIndex + 4;
    }

    public void handleVersionState() {
        if(buff.length() < splitIndex + 5) {
            searchIndex = buff.length();
            return;
        }

        rpcMessage.getHeader().setVersion(buff.byteAt(4 + splitIndex));
        state = BIT_FLAG;
        searchIndex = splitIndex + 5;
    }

    public void handleBitFlagState() {
        if(buff.length() < 6 + splitIndex) {
            searchIndex = buff.length();
            return;
        }

        rpcMessage.getHeader().setVersion(buff.byteAt(5 + splitIndex));
        rpcMessage.getHeader().setHb(buff.byteAt(5 + splitIndex));
        rpcMessage.getHeader().setOw(buff.byteAt(5 + splitIndex));
        rpcMessage.getHeader().setRp(buff.byteAt(5 + splitIndex));
        state = STATUS_CODE;
        searchIndex = splitIndex + 6;
    }

    public void handleStatusCodeState() {
        if(buff.length() < 7 + splitIndex) {
            searchIndex = buff.length();
            return;
        }

        rpcMessage.getHeader().setStatusCode(buff.byteAt(6 + splitIndex));
        state = RESERVED;
        searchIndex = splitIndex + 7;
    }

    public void handleReservedState() {
        if(buff.length() < 8 + splitIndex) {
            searchIndex = buff.length();
            return;
        }

        rpcMessage.getHeader().setStatusCode(buff.byteAt(7 + splitIndex));
        state = MESSAGE_ID;
        searchIndex = splitIndex + 8;
    }

    public void handleMessageIdState() {
        if(buff.length() < 16 + splitIndex) {
            searchIndex = buff.length();
            return;
        }

        long mid = ByteUtil.bytesToLong(buff.buffer(), splitIndex + 8);
        rpcMessage.getHeader().setMid(mid);
        state = BODY_SIZE;
        searchIndex = splitIndex + 16;
    }

    public void handleBodySizeState() {
        if(buff.length() < 20 + splitIndex) {
            searchIndex = buff.length();
            return;
        }

        int size = ByteUtil.bytesToInt(buff.buffer(), 16 + splitIndex);
        rpcMessage.getHeader().setBodySize(size);
        state = BODY;
        searchIndex = 20 + splitIndex;
    }

    public void handleBodyState() {
        int headerSize = rpcMessage.getHeader().getHeaderSize();
        int bodySize = rpcMessage.getHeader().getBodySize();
        if(buff.length() < headerSize + bodySize + splitIndex) {
            searchIndex = buff.length();
            return;
        }

        RpcSerialization<RpcBody> deserializer = serializerRegistry.findSerializerByType(rpcMessage.getHeader().getSt());
        if(deserializer == null) {
            throw new ProtocolException("Can not find the specific deserializer");
        }
        RpcBody rpcBody = deserializer.deserialize(buff.buffer(), 20 + splitIndex);
        rpcMessage.setBody(rpcBody);
        searchIndex = headerSize + bodySize + splitIndex;
        state = END;
    }

    public void handleEndState(List<RpcMessage> messages) {
        messages.add(rpcMessage);
        splitIndex = searchIndex;
        rpcMessage = null;
        state = START;
    }
}
