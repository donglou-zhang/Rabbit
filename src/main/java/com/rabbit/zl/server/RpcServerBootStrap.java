package com.rabbit.zl.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Vincent
 * Created  on 2017/11/23.
 */
public class RpcServerBootStrap {

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("spring-conf.xml");
    }
}
