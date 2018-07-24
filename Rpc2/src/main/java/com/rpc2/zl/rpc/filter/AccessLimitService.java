package com.rpc2.zl.rpc.filter;

import com.rpc2.zl.rpc.common.RpcInvocation;

/**
 * 限流接口
 * 调用端自定义实现
 * Created by Vincent on 2018/7/24.
 */
public interface AccessLimitService {

    void acquire(RpcInvocation invocation);
}
