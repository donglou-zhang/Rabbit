package com.rpc2.zl.rpc.registry.zookeeper;

import com.rpc2.zl.rpc.registry.RegistryModel;
import com.rpc2.zl.rpc.registry.RpcDiscovery;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

/**
 * Created by Vincent on 2018/7/11.
 */
public class ZkDiscoveryService extends AbstractZkService implements RpcDiscovery{

    private ZkClient zkClient;

    public ZkDiscoveryService(String discoveryHost, int discoveryPort) {
        this.zkClient = buildZkClient(discoveryHost, discoveryPort);
    }

    @Override
    public List<RegistryModel> getRegistryServices(String serviceName) {
        return null;
    }
}
