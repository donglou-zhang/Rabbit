package com.rabbit.zl.common.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * To get all the configuration and inject RpcConfig to other Class when needed
 *
 * @author Vincent
 * Created  on 2017/12/18.
 */
//@Component("rpcConfig")
public class Config {

    @Getter
    @Value("${registry.host}")
    private String registerHost;

    @Getter
    @Value("${registry.port}")
    private int registerPort;

    @Getter
    @Value("${application}")
    private String application;
}
