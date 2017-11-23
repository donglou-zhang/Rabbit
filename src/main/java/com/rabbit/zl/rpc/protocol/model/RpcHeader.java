package com.rabbit.zl.rpc.protocol.model;

import java.io.Serializable;

import com.rabbit.zl.common.util.ByteUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a RPC header field.
 * <p>
 *
 * The RPC header fields use the generic format as follows:
 * <pre>
 * 000-------------------------------------------------------------------015-------------------------------------------------------------------031
 * |                                 magic                                |                                header size                           |
 * 032--------------------------------039-----------044----045----046----047--------------------------------055--------------------------------063
 * |                version            |      st     |  hb  |  ow  |  rp  |        status code               |            reserved               |
 * 064-----------------------------------------------------------------------------------------------------------------------------------------095
 * |                                                                 message id                                                                  |
 * |                                                                                                                                             |
 * 096-----------------------------------------------------------------------------------------------------------------------------------------127
 * |                                                                  body size                                                                  |
 * 128-----------------------------------------------------------------------------------------------------------------------------------------159
 *
 * magic: designed for decode
 * header size: protocol header size, designed for expanding
 * status code: response message status code
 * reserved: for byte alignment
 *
 * st = serialization type.(from 40 to 44)
 * hb = heartbeat flag, set '0000 0100' means it is a heartbeat message, designed for long connection.
 * ow = one way   flag, set '0000 0010' means it is one way message, the client doesn't wait for a response.
 * rp = response  flag, set '0000 0001' means it is response message, otherwise it's a request message.
 * </pre>
 *
 * @author Vincent
 * Created  on 2017/11/10.
 */
@ToString
@EqualsAndHashCode(of = {"magic", "headerSize", "version", "st", "hb", "ow", "rp", "statusCode", "reserved", "mid", "bodySize"})
public class RpcHeader implements Serializable{

    /**
     * for java serialization, when carry out deserialization, JVM will compare the received serialVersionUID with local serialVersionUID,
     if same, it can be deserialized.
     */
    private static final long serialVersionUID = -67119913240566784L;

    private static final byte   ST_MASK            = (byte) 0xf8;
    private static final byte   HB_MASK            = (byte) 0x04;
    private static final byte   OW_MASK            = (byte) 0x02;
    private static final byte   RP_MASK            = (byte) 0x01;
    public  static final short  MAGIC              = (short) 0xcaf6;
    public  static final byte MAGIC_BIT_0 = ByteUtil.shortToBytes(MAGIC)[0];
    public  static final byte MAGIC_BIT_1 = ByteUtil.shortToBytes(MAGIC)[1];
    public  static final short  HEADER_SIZE        = (short) 20;
    public  static final byte   VERSION            = (byte)  1;

    @Getter @Setter private short magic = MAGIC;
    @Getter @Setter private short headerSize = HEADER_SIZE;
    @Getter @Setter private byte version = VERSION;
    @Getter         private byte st = (byte)1;
    @Getter         private byte hb = (byte)0;
    @Getter         private byte ow = (byte)0;
    @Getter         private byte rp = (byte)0;
    @Getter @Setter private byte statusCode = (byte)0;
    @Getter @Setter private byte reserved = (byte)0;
    @Getter @Setter private long mid = (long)0;
    @Getter @Setter private int bodySize = (int)0;

    public void setSt(byte st) {
        this.st = (byte)(st & ST_MASK);
    }

    public void setHb() {
        this.hb = HB_MASK;
    }

    public void setHb(byte hb) {
        this.hb = (byte)(hb & HB_MASK);
    }

    public boolean isHb() {
        return this.hb == HB_MASK;
    }

    public void setOw() {
        this.ow = OW_MASK;
    }

    public void setOw(byte ow) {
        this.ow = (byte)(ow & OW_MASK);
    }

    public boolean isOw() {
        return this.ow == OW_MASK;
    }

    public void setRp() {
        this.rp = RP_MASK;
    }

    public void setRp(byte rp) {
        this.rp = (byte)(rp & RP_MASK);
    }

    public boolean isRp() {
        return this.rp == RP_MASK;
    }
}
