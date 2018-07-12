package com.rpc2.zl.rpc.registry;

/**
 * Created by Vincent on 2018/7/10.
 */
public interface RpcRegistry {

    void register(RegistryModel registryModel);

    void unregister(RegistryModel registryModel);

}
