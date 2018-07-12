package com.rabbit.zl.common.util;

/**
 * Byte util can provides methods to manipulate bytes
 *
 * @author Vincent
 * Created  on 2017/11/14.
 */
public class ByteUtil {

    /**
     * Get a byte array from a short
     *
     * @param s short
     * @return byte array
     */
    public static final byte[] shortToBytes(short s) {
        byte[] b = new byte[]{0, 0};
        shortToBytes(s, b, 0);
        return b;
    }

    /**
     * Set a byte array at specific offset from a short
     *
     * @param s
     * @param bytes
     * @param offset
     */
    public static final void shortToBytes(short s, byte[] bytes, int offset) {
        // short convert to byte, it will use low bits of short
        bytes[offset+1] = (byte)s;
        // fill in 0 in the high bits
        bytes[offset] = (byte)(s>>>8);
    }

    /**
     * Get a short from the bytes given.
     *
     * @param bytes
     * @return
     */
    public static final short bytesToShort(byte[] bytes) {
        return bytesToShort(bytes, 0);
    }

    public static final short bytesToShort(byte[] bytes, int off) {
        short s1 = (short)(bytes[off] & 0xff);
        short s2 = (short)(bytes[off+1] & 0xff);
        s1<<=8;
        return (short)(s1 | s2);
    }

    /**
     * Get a byte array from int
     *
     * @param i
     * @return
     */
    public static final byte[] intToBytes(int i) {
        byte[] b = new byte[]{0, 0, 0, 0};
        intToBytes(i, b, 0);
        return b;
    }

    //高位在前，低位在后
    public static final void intToBytes(int i, byte[] bytes, int offset) {
        bytes[offset+3] = (byte)i;
        bytes[offset+2] = (byte)(i>>>8);
        bytes[offset+1] = (byte)(i>>>16);
        bytes[offset] = (byte)(i>>>24);
    }

    /**
     * Get a int from the bytes given.
     *
     * @param bytes
     * @return
     */
    public static final int bytesToInt(byte[] bytes) {
        return bytesToInt(bytes, 0);
    }

    public static final int bytesToInt(byte[] bytes, int off) {
        int ret = (int)(bytes[3 + off] & 0xff);
        for(int i=2; i>-1; i--) {
            ret = ((int)((bytes[i + off] & 0xff) << (8*(3-i))) | ret);
        }
        return ret;
    }

    /**
     * Get a byte array from long
     *
     * @param l
     * @return
     */
    public static final byte[] longToBytes(long l) {
        byte[] b = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        longToBytes(l, b, 0);
        return b;
    }

    public static final void longToBytes(long l, byte[] bytes, int offset) {
        for(int i=7; i>-1;i--) {
            bytes[offset+i] = (byte)(i>>>((7-i)*8));
        }
    }

    /**
     * Get a long from the bytes given
     *
     * @param bytes
     * @return
     */
    public static final long bytesToLong(byte[] bytes) {
        return bytesToLong(bytes, 0);
    }

    public static final long bytesToLong(byte[] bytes, int off) {
        long ret = (long)(bytes[off + 7] & 0xff);
        for(int i=6; i>-1; i--) {
            ret = ((long)((bytes[off + i] & 0xff) << (8*(7-i))) | ret);
        }
        return ret;
    }
}
