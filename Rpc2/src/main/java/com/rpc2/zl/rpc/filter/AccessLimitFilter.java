package com.rpc2.zl.rpc.filter;

import com.rpc2.zl.common.constant.ConstantConfig;
import com.rpc2.zl.rpc.common.RpcInvocation;
import com.rpc2.zl.rpc.common.RpcInvoker;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by Vincent on 2018/7/24.
 */
@ActiveFilter(group = {ConstantConfig.CONSUMER})
public class AccessLimitFilter implements RpcFilter{

    @Override
    public Object invoke(RpcInvoker invoker, RpcInvocation invocation) {
        // 获取AccessLimitService的所有实现类
        List<AccessLimitService> accessLimitServiceList = SpringFactoriesLoader.loadFactories(AccessLimitService.class, null);
        if(!CollectionUtils.isEmpty(accessLimitServiceList)) {
            AccessLimitService accessLimitService = accessLimitServiceList.get(0);
            accessLimitService.acquire(invocation);
        }
        Object rpcResponse = invoker.invoke(invocation);
        return rpcResponse;
    }
}
