package com.rpc2.zl.rpc.protocol;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Vincent on 2018/7/12.
 */
public class RpcResponse {

    @Getter @Setter
    private String requestId;

    @Getter @Setter
    private Throwable error;

    @Getter @Setter
    private Object result;
}
