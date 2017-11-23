package com.rabbit.zl.server;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import com.rabbit.zl.rpc.executor.ServerRpcExecutorFactory;
import com.rabbit.zl.rpc.invoke.RpcInvoker;
import com.rabbit.zl.rpc.registry.RpcRegistry;
import com.rabbit.zl.rpc.registry.RpcService;
import com.rabbit.zl.rpc.transmission.RpcAcceptor;
import com.rabbit.zl.serverStub.RpcProcessor;
import com.rabbit.zl.serverStub.ServerRpcInvoker;
import com.rabbit.zl.serverStub.ServerRpcProcessor;
import com.rabbit.zl.transfer.netty.NettyServerAcceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * Rpc server, find all the services which is annotated with {@link RpcService}
 * and then register all the services
 *
 * @author Vincent
 * Created  on 2017/11/11.
 */
public class RpcServer implements ApplicationContextAware, InitializingBean{

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);

    // Store the mapping between rpcInterface and it's implementation bean
    private Map<String, Object> serviceBeanMap = new HashMap<>();

    @Getter @Setter private String serverHost;

    @Getter @Setter private int serverPort;

    @Getter @Setter private RpcProcessor processor;

    @Getter @Setter private RpcAcceptor acceptor;

    @Getter @Setter private RpcInvoker invoker;

    @Getter @Setter
    @Autowired
    @Qualifier("serviceRegistry")
    private RpcRegistry registry;

    @Getter @Setter private ServerRpcExecutorFactory executorFactory;

    public RpcServer(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;

        processor = new ServerRpcProcessor();
        invoker = new ServerRpcInvoker();
        acceptor = new NettyServerAcceptor();
    }

    public void init() {
        invoker.setRegistry(registry);
        invoker.setServiceBeanMap(serviceBeanMap);
        acceptor.setAddress(serverHost, serverPort);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //TODO Register all the services
        for(String key : serviceBeanMap.keySet()) {
            System.out.println("To register service: " + key);
        }
        System.out.println("afterPropertiesSet finished!");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //Get all the SpringBean with "RpcService" annotation
        Map<String, Object> handlerMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if(handlerMap.size() > 0) {
            for(Object serviceBean : handlerMap.values()) {
                String rpcInterface = serviceBean.getClass().getAnnotation(RpcService.class).value().getName();
                serviceBeanMap.put(rpcInterface, serviceBean);
            }
        }
        System.out.println("setApplicationContext finished!");
    }
}
