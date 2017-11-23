package com.rabbit.zl.rpc.registry.zookeeper;

/**
 * The related configuration about zookeeper
 *
 * @author Vincent
 * Created  on 2017/11/12.
 */
public final class Constant {

    /**
     * Root path of znode in zookeeper
     */
    public static final String ZK_REGISTRY_PATH = "/registry";

    /**
     * Root path of the registered services in zookeeper
     */
    public static final String ZK_DATA_PATH = ZK_REGISTRY_PATH + "/services";

    /**
     * the timeout of zookeeper client
     */
    public static final int ZK_SESSION_TIMEOUT = 5000;

    public static final String PATH_SEPERATOR = "/";
}
