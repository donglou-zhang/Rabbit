package com.rpc2.zl.rpc.protocol;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Vincent on 2018/7/11.
 */
public class RpcMessage implements Serializable {

    @Getter @Setter
    private RpcMessageHeader header;

    @Getter @Setter
    private Object body;

}
