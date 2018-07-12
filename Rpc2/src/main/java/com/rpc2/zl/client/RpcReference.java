package com.rpc2.zl.client;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Vincent on 2018/7/10.
 */
@Target({ElementType.TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcReference {

    boolean isSync() default true;

    //客户端最大并发数
    int cliMaxExecuteCount() default 0;

    //服务降级的伪装者类对象
    Class<?> fallbackServiceClazz() default Object.class;
}
