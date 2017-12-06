package com.rabbit.zl.common.serialization;

import com.rabbit.zl.common.exception.ProtocolException;
import com.rabbit.zl.rpc.protocol.model.RpcBody;

/**
 * Serialize object to bytes or deserialize object from bytes
 *
 * @author Vincent
 * Created on 2017/11/12.
 */
public interface RpcSerialization<T> {

    /**
     *
     * @return serialization type
     */
    byte type();

    /**
     * Serialize object to bytes
     *
     * @param obj
     * @return
     * @throws ProtocolException
     */
    byte[] serialize(RpcBody obj) throws ProtocolException;

    /**
     * Deserialize object from bytes
     *
     * @param data
     * @return
     * @throws ProtocolException
     */
    T deserialize(byte[] data) throws ProtocolException;

    /**
     * Deserialize object from bytes at specific offset
     *
     * @param data
     * @param off
     * @return
     * @throws ProtocolException
     */
    T deserialize(byte[] data, int off) throws ProtocolException;
}
