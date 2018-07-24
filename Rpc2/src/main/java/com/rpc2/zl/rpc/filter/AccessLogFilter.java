package com.rpc2.zl.rpc.filter;

import com.rpc2.zl.common.constant.ConstantConfig;
import com.rpc2.zl.rpc.common.RpcInvocation;
import com.rpc2.zl.rpc.common.RpcInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Vincent on 2018/7/24.
 */
@ActiveFilter(group = {ConstantConfig.PROVIDER, ConstantConfig.CONSUMER})
public class AccessLogFilter implements RpcFilter {

    private static final Logger logger = LoggerFactory.getLogger(AccessLogFilter.class);

    @Override
    public Object invoke(RpcInvoker invoker, RpcInvocation invocation) {
        logger.info("before call");
        Object rpcResponse = invoker.invoke(invocation);
        logger.info("after call");
        return rpcResponse;
    }
}
