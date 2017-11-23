package com.rabbit.zl.rpc.registry;

/**
 * @author Vincent
 * Created  on 2017/11/12.
 */
public interface RpcRegistry {

    void register(RpcRegistryService service);

    void unregister(RpcRegistryService service);
}
