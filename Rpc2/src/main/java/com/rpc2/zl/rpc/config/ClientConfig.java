package com.rpc2.zl.rpc.config;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Vincent on 2018/7/10.
 */
public class ClientConfig {

    @Getter @Setter
    private String remoteHost;
    @Getter @Setter
    private int remotePort;
    @Getter @Setter
    protected long connectTimeoutMillis = 6000;

    @Getter @Setter
    private String registryHost;
    @Getter @Setter
    private int registryPort;

    public ClientConfig() {

    }

    public ClientConfig(String remoteHost, int remotePort, String registryHost, int registryPort) {
        this.registryHost = registryHost;
        this.registryPort = registryPort;
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    public ClientConfig(String registryHost, int registryPort) {
        this.registryHost = registryHost;
        this.registryPort = registryPort;
    }
}
