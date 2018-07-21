package com.rpc2.zl.server;

import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vincent on 2018/7/21.
 */
public class RpcServerInitializer implements ApplicationContextAware, InitializingBean{

    @Getter
    private final Map<String, Object> handlerMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if(null != serviceBeanMap) {
            for(Object serviceBean : serviceBeanMap.values()) {
                Class<?>[] interfaces = serviceBean.getClass().getInterfaces();
                for(Class<?> clazz : interfaces) {
                    String interfaceName = clazz.getName();
                    handlerMap.put(interfaceName, serviceBean);
                }
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //TODO 将service注册到register
    }
}
