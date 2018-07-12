package com.rpc2.zl.rpc.registry;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Vincent on 2018/7/11.
 */
public class RegistryModel {

    @Getter @Setter private String application;

    @Getter @Setter private String rpcInterface;

    @Getter @Setter private String methodName;

    @Getter @Setter private String version;

    @Getter @Setter private String serverHost;

    @Getter @Setter private int serverPort;

    @Getter @Setter private int weight;

    public RegistryModel() {}

    public RegistryModel(String application, String rpcInterface, String methodName, String
            version, String serverHost, int serverPort, int weight) {
        this.application = application;
        this.rpcInterface = rpcInterface;
        this.methodName = methodName;
        this.version = version;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.weight = weight;
    }
}
