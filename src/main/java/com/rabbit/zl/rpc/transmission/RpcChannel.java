package com.rabbit.zl.rpc.transmission;

import com.rabbit.zl.common.exception.RpcException;
import com.rabbit.zl.rpc.protocol.model.RpcMessage;

import java.util.List;

/**
 * Rpc channel, provides the function: <br/>
 * -Write <code>RpcMessage<code/> to remote peer
 * -Read <code>RpcMessage<code/> from remote peer
 *
 * @author Vincent
 * Created on 2017/11/13.
 */
public interface RpcChannel {

    /**
     * Write rpc message and encode it to bytes to remote peer of the channel
     *
     * @param msg
     * @throws RpcException
     */
    void write(RpcMessage msg) throws RpcException;

    /**
     * Read bytes and decode it to rcp message(s) from remote peer of the channel
     *
     * @param bytes
     * @return
     */
    List<RpcMessage> read(byte[] bytes);
}
