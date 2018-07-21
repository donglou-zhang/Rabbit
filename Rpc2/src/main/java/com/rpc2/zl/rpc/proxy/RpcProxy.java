package com.rpc2.zl.rpc.proxy;

import com.rpc2.zl.client.RpcReference;
import com.rpc2.zl.common.util.PropertyUtil;
import com.rpc2.zl.rpc.config.ClientConfig;
import com.rpc2.zl.rpc.common.loadbalance.*;
import com.rpc2.zl.rpc.registry.RegistryModel;
import com.rpc2.zl.rpc.registry.RpcDiscovery;
import com.rpc2.zl.rpc.registry.zookeeper.ZkDiscoveryService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Vincent on 2018/7/10.
 */
public class RpcProxy <T> implements InvocationHandler {

    private Class<T> clazz;

    private ClientConfig clientConfig = null;

    private RpcReference reference = null;

    private static LoadBalanceService loadBalanceService = new RoundRobinLoadBalance();

    public RpcProxy(ClientConfig config) {
        this.clientConfig = config;
    }

    public RpcProxy(ClientConfig config, RpcReference reference) {
        this.clientConfig = config;
        this.reference = reference;
    }

    public RpcProxy(Class<T> clazz, ClientConfig config, RpcReference reference) {
        this.clazz = clazz;
        this.clientConfig = config;
        this.reference = reference;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String serviceName = this.clazz.getName();
        String discoveryHost = PropertyUtil.getProperty("discovery.host");
        int discoveryPort = Integer.parseInt(PropertyUtil.getProperty("discovery.port"));
        RpcDiscovery discovery = new ZkDiscoveryService(discoveryHost, discoveryPort);
        List<RegistryModel> services = discovery.getRegistryServices(serviceName);
        int index = loadBalanceService.index(services.size());
        RegistryModel registryModel = services.get(index);

        this.clientConfig.setRemoteHost(registryModel.getServerHost());
        this.clientConfig.setRegistryPort(registryModel.getServerPort());
        RpcHystrixCommand command = new RpcHystrixCommand(method, args, reference, clientConfig);
        return command.execute();
    }
}
