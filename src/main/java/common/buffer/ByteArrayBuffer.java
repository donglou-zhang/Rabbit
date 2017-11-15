package common.buffer;

import java.io.Serializable;

public final class ByteArrayBuffer implements Serializable{

    private byte[] buffer;

    // Currently used byte array length
    private int length;

    public ByteArrayBuffer(int capacity) {
        checkCapacity(capacity);
        this.buffer = new byte[capacity];
    }

    private void checkCapacity(int capacity) {
        if(capacity < 0) {
            throw new IllegalArgumentException("Buffer capacity should be positive");
        }
    }

    public void clear() {
        this.length = 0;
    }

    public void reset(int capacity) {
        checkCapacity(capacity);
        clear();
        this.buffer = new byte[capacity];
    }

    /**
     * Appends <code>len<code/> bytes to this buffer from the giver byte array, starting at index <code>off<code/>
     * If current capacity is not enough, it will be increased, to accommodate all <code>len<code/> bytes
     *
     * @param b  The bytes to be append
     * @param off  The index of
     * @param len
     * @return
     */
    public ByteArrayBuffer append(final byte[] b, int off, int len) {
        if(b == null) {
            return this;
        }

        if((off | len | (b.length - off) | (off + len) | (b.length - off - len)) < 0) {
            throw new IndexOutOfBoundsException("off = " + off + ", len = " + len + ", byte length = " + b.length);
        }

        if(len == 0) {
            return this;
        }

        // if capacity is not enough, expand it
        int newLen = this.length + len;
        if(newLen > this.buffer.length) {
            expandCapacity(newLen);
        }

        System.arraycopy(b, off, this.buffer, this.length, len);
        this.length = newLen;

        return this;

    }

    public ByteArrayBuffer append(final byte[] b) {
        if(b == null) {
            return this;
        }
        return append(b, 0, b.length);
    }

    /**
     * Get current used buffer total length
     *
     * @return
     */
    public int length() {
        return this.length;
    }

    /**
     * Get current buffer total length
     *
     * @return
     */
    public int capacity() {
        return this.buffer.length;
    }

    /**
     * Return the <code>byte<code/> value in this buffer at the specified index, just like String.charAt
     * The index must be greater than or equal to <code>0<code/>, and less than the total length of this buffer
     *
     * @param i
     * @return
     */
    public byte byteAt(int i) {
        return this.buffer[i];
    }

    /**
     * Return reference to the underlying byte array
     *
     * @return
     */
    public byte[] buffer() {
        return this.buffer;
    }

    /**
     * Return new sub array of bytes from the buffer, with boundary from start to end
     * if start == end, return a zero byte array.
     *
     * @param start The begin index, inclusive
     * @param end The end index, exclusive
     * @return byte array
     */
    public byte[] subByteArray(int start, int end) {
        if(start < 0 || end < 0 || start > end || end > length()) {
            throw new IllegalArgumentException("Input start and end are not legal, start = " + start +", end = " + end + ", length = " + length());
        }

        int len = end - start;
        if(len == 0) {
            return new byte[0];
        }

        byte[] ret = new byte[len];
        System.arraycopy(this.buffer, start, ret, 0, len);
        return ret;
    }

    private void expandCapacity(int newCapacity) {
        byte[] newBuffer = new byte[Math.max(this.buffer.length << 1, newCapacity)];
        System.arraycopy(this.buffer, 0, newBuffer, 0, this.length);
        this.buffer = newBuffer;
    }
}
