package com.rabbit.zl.common.serialization;

import com.rabbit.zl.common.compress.JdkZip;
import com.rabbit.zl.common.exception.ProtocolException;
import com.rabbit.zl.rpc.protocol.model.RpcBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * implement serialization by Jdk's serialization
 *
 * @author Vincent
 * Created  on 2017/11/12.
 */
public class JdkSerializer implements RpcSerialization<RpcBody> {

    private final static Logger LOGGER = LoggerFactory.getLogger(JdkSerializer.class);

    public byte type() {
        return 2;
    }

    /**
     * serialize and compress，to reduce network traffic
     *
     * @param obj
     * @return
     */
    public byte[] serialize(RpcBody obj) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.close();
            byte[] bytes = bos.toByteArray();
            return new JdkZip().zip(bytes);
        } catch (IOException e) {
            LOGGER.error("", e);
        }
        return null;
    }

    /**
     * unzip and then deserialize
     *
     * @param bytes
     * @return
     */
    public RpcBody deserialize(byte[] bytes) {
        byte[] new_bytes = new JdkZip().unzip(bytes);
        ByteArrayInputStream bis = new ByteArrayInputStream(new_bytes);
        try {
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (RpcBody) ois.readObject();
        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return null;
    }

    public RpcBody deserialize(byte[] data, int off) throws ProtocolException {
        return null;
    }
}
