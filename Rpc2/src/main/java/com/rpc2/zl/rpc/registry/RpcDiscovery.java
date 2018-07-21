package com.rpc2.zl.rpc.registry;

import java.util.List;

/**
 * Created by Vincent on 2018/7/17.
 */
public interface RpcDiscovery {
    List<RegistryModel> getRegistryServices(String serviceName);
}
