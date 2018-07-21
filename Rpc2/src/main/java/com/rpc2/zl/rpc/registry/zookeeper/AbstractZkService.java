package com.rpc2.zl.rpc.registry.zookeeper;

import org.I0Itec.zkclient.ZkClient;

/**
 * Created by Vincent on 2018/7/11.
 */
public abstract class AbstractZkService {

    protected ZkClient buildZkClient(String host, int port) {
        return new ZkClient(host + ":" + String.valueOf(port));
    }
}
