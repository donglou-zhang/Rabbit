package rpc.executor;

import common.exception.RpcException;
import rpc.monitor.MonitoringExecutorService;
import rpc.registry.RpcDiscovery;
import rpc.registry.RpcRegistry;

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

    void setDiscovery(RpcDiscovery discovery);

    void shutdown();
}
