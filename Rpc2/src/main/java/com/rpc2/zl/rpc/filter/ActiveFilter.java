package com.rpc2.zl.rpc.filter;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Vincent on 2018/7/24.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface ActiveFilter {

    String[] group() default {};
    String[] value() default {};
    int order() default 999999999;
}
