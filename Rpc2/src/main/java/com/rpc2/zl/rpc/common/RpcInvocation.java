package com.rpc2.zl.rpc.common;

import java.util.Map;

/**
 * Created by Vincent on 2018/7/11.
 */
public interface RpcInvocation {

    String getRequestId();

    String getClassName();

    String getMethodName();

    Object[] getParameters();

    Class<?>[] getParameterTypes();

    int getMaxExecutesCount();

    Map<String,Object> getContextParameters();
}
