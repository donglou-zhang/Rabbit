package com.rabbit.zl.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Start project, and read the configuration
 *
 * @author Vincent
 * Created  on 2017/11/23.
 */
public class RpcServerBootStrap {

    @SuppressWarnings("resource")
    public static void main(String[] args) throws IOException {
        new ClassPathXmlApplicationContext("spring-config.xml");
    }
}
