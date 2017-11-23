package com.rabbit.zl.common.serialization;

import com.rabbit.zl.common.exception.ProtocolException;

/**
 * Serialize object to bytes or deserialize object from bytes
 *
 * @author Vincent
 * Created on 2017/11/12.
 */
public interface RpcSerialization {

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
    byte[] serialize(Object obj) throws ProtocolException;

    /**
     * Deserialize object from bytes
     *
     * @param data
     * @return
     * @throws ProtocolException
     */
    Object deserialize(byte[] data) throws ProtocolException;

    /**
     * Deserialize object from bytes at specific offset
     *
     * @param data
     * @param off
     * @return
     * @throws ProtocolException
     */
    Object deserialize(byte[] data, int off) throws ProtocolException;
}
