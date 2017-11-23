package com.rabbit.zl.rpc.protocol.codec;

import com.rabbit.zl.common.buffer.ByteArrayBuffer;
import lombok.Getter;
import lombok.Setter;

/**
 * It contains three index for concrete protocol decoder using:
 * - splitIndex : The separator index position according to specific protocol, indicates next byte nearby last complete protocol object.
 * - searchIndex : The cursor index position for protocol process, indicates next byte would be processed by protocol codec
 * - stateIndex : The index position for protocol' current state which is processed.
 *
 * @author Vincent
 * Created on 2017/11/14.
 */
public class AbstractProtocolDecoder {

    protected static final int START = 0;
    protected static final int END = -1;

    @Getter @Setter protected int splitIndex = 0;
    @Getter @Setter protected int searchIndex = 0;
    @Getter @Setter protected int stateIndex = 0;

    @Getter  protected int state = START;
    @Getter @Setter protected int defaultBufferSize = 2048;
    @Getter @Setter protected int maxSize = defaultBufferSize * 1024;
    @Getter @Setter protected ByteArrayBuffer buff = new ByteArrayBuffer(defaultBufferSize);

    public void reset() {
        splitIndex = 0;
        searchIndex = 0;
        stateIndex = 0;
        state = START;
        buff = new ByteArrayBuffer(defaultBufferSize);
    }

    public void adaptBuffer() {
        if(splitIndex > 0 && splitIndex < buff.length()) {
            byte[] tailBytes = buff.subByteArray(splitIndex, buff.length());
            buff.clear();
            buff.append(tailBytes);
            splitIndex = 0;
            searchIndex = buff.length();
        }

        if(splitIndex > 0 && splitIndex == buff.length()) {
            buff.clear();
            splitIndex = searchIndex = 0;
        }

        if(buff.length() == 0 && buff.capacity() > maxSize * 2) {
            buff.reset(defaultBufferSize);
        }
    }




}
