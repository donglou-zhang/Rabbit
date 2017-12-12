package com.rabbit.zl.rpc.executor;

import com.rabbit.zl.common.exception.RpcException;
import lombok.Getter;
import lombok.Setter;
import com.rabbit.zl.rpc.monitor.MonitoringExecutorService;
import com.rabbit.zl.rpc.registry.RpcDiscovery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * On the server side, after get the rpc request, a new thread will be created to carry out the request
 *
 * @author Vincent
 * Created  on 2017/11/21.
 */
@Component
public class ServerRpcExecutorFactory implements RpcExecutorFactory {

    private Map<String, MonitoringExecutorService> monitorMap;

    public ServerRpcExecutorFactory() {
        this.monitorMap = new ConcurrentHashMap<>();
    }

    @Override
    public MonitoringExecutorService getMonitorExecutor(String service) throws RpcException {
        return getExecutor(service);
    }

    private MonitoringExecutorService getExecutor(String service) {
        MonitoringExecutorService mes = monitorMap.get(service);
        if(mes == null) {
            synchronized (this) {
                if(mes == null) {
                    //TODO Configurations like poolSize can be set by other ways
                    RpcThreadPoolExecutor executor = new RpcThreadPoolExecutor(1,10,60, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(10), new CustomizedThreadFactory("Rabbit"));

                    //If set true, when the core thread is idle, it will also be closed after "keepAliveTime".
                    executor.allowCoreThreadTimeOut(true);
                    mes = executor;
                    monitorMap.put(service, executor);
                }
            }
        }
        return mes;
    }

    @Override
    public void shutdown() {
        for(MonitoringExecutorService mes : monitorMap.values()) {
            mes.shutdown();
        }
    }
}
