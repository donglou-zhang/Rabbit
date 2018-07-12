package com.rpc2.zl.rpc.registry.zookeeper;

import com.rpc2.zl.rpc.registry.RegistryModel;
import com.rpc2.zl.rpc.registry.RpcRegistry;
import org.I0Itec.zkclient.ZkClient;

/**
 * Created by Vincent on 2018/7/11.
 */
public class ZkRegistryService extends AbstractZkService implements RpcRegistry {

    private ZkClient zkClient;

    public ZkRegistryService(String registryHost, int registryPort) {
        this.zkClient = buildZkClient(registryHost, registryPort);
    }

    @Override
    public void register(RegistryModel registryModel) {

    }

    @Override
    public void unregister(RegistryModel registryModel) {

    }
}
