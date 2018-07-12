package com.rpc2.zl.rpc.protocol;

/**
 * Created by Vincent on 2018/7/12.
 */
public interface RpcResponseCallback {

    void onSuccess(Object response);

    void onException(RuntimeException ex);
}
