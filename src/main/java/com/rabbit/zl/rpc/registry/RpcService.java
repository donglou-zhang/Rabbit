package com.rabbit.zl.rpc.registry;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The class which has {@link RpcService} annotation will be scanned by Spring
 *
 * @author Vincent
 * Created  on 2017/11/11.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcService {

    Class<?> value();

    String version() default "1.0.0";

    int weight() default 1;
}
