package com.rpc2.zl.remoting.serialize;

/**
 * Created by Vincent on 2018/7/10.
 */
public interface RpcSerializer {

    /**
     * 将对象obj序列化成字节数组
     * @param obj
     * @return
     */
    public byte[] serialize(Object obj);

    /**
     * 将字节数组data反序列化成目标对象
     * @param data
     * @return
     */
    public Object deserialize(byte[] data);

    /**
     * 将字节数组data反序列化成目标对象，其中字节数组从偏移位置off初开始
     * @param data
     * @param off
     * @return
     */
    public Object deserialize(byte[] data, int off);

}
