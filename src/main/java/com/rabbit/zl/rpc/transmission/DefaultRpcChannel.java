package com.rabbit.zl.rpc.transmission;

import com.rabbit.zl.common.exception.RpcException;
import com.rabbit.zl.rpc.protocol.model.RpcMessage;

import java.util.List;

public class DefaultRpcChannel implements RpcChannel{
    @Override
    public void write(RpcMessage msg) throws RpcException {

    }

    @Override
    public List<RpcMessage> read(byte[] bytes) {
        return null;
    }
}
