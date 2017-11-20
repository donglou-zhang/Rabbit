package rpc.executor;


import common.exception.RpcException;
import lombok.Getter;
import lombok.Setter;
import rpc.monitor.MonitoringExecutorService;
import rpc.registry.RpcDiscovery;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerRpcExecutorFactory implements RpcExecutorFactory {

    @Getter @Setter private RpcDiscovery discovery;

    private Map<String, MonitoringExecutorService> monitorMap;

    public ServerRpcExecutorFactory() {
        this.monitorMap = new ConcurrentHashMap<>();
    }

    @Override
    public MonitoringExecutorService getMonitorExecutor(String service) throws RpcException {
        return getExecutor(service);
    }

    private MonitoringExecutorService getExecutor(String service) {
        String application = service.split("/")[0];
        String rpcInterface = service.split("/")[1];
        MonitoringExecutorService mes = monitorMap.get(service);
        if(mes == null) {
            synchronized (this) {
                if(mes == null) {
                    List<String> serviceList = discovery.discoverAll(application, rpcInterface);
                    if(serviceList.size() == 0) {
                        throw new RpcException(RpcException.SERVER_ERROR, "No registered rpc service!");
                    }


                }
            }
        }
        return null;
    }

    @Override
    public void shutdown() {
        for(MonitoringExecutorService mes : monitorMap.values()) {
            mes.shutdown();
        }
    }
}
