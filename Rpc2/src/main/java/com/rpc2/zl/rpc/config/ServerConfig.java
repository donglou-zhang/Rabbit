package com.rpc2.zl.rpc.config;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Vincent on 2018/7/18.
 */
public class ServerConfig {

    @Getter @Setter
    private String serverHost;
    @Getter @Setter
    private int serverPort;

    @Getter @Setter
    private String registryHost;
    @Getter @Setter
    private int registryPort;
}
