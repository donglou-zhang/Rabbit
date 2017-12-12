package com.rabbit.zl.rpc.executor;

import com.rabbit.zl.common.exception.RpcException;
import com.rabbit.zl.rpc.registry.RpcDiscovery;
import com.rabbit.zl.rpc.monitor.MonitoringExecutorService;

/**
 *
 * @author Vincent
 * Created  on 2017/11/20.
 */
public interface RpcExecutorFactory {

    /**
     *
     * @param service application+"/"+rpcInterface
     * @return
     * @throws RpcException
     */
    MonitoringExecutorService getMonitorExecutor(String service) throws RpcException;

    void shutdown();
}
