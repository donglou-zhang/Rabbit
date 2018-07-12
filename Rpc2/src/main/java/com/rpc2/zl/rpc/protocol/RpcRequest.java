package com.rpc2.zl.rpc.protocol;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Created by Vincent on 2018/7/11.
 */
public class RpcRequest {
    @Getter @Setter
    private String requestId;

    @Getter @Setter
    private String className;

    @Getter @Setter
    private String methodName;

    @Getter @Setter
    private Class<?>[] parameterTypes;

    @Getter @Setter
    private Object[] parameters;

    @Getter @Setter
    private int maxExecutesCount;

    @Getter @Setter
    private Map<String,Object> contextParameters;
}
