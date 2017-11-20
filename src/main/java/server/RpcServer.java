package server;

import io.netty.channel.EventLoopGroup;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import rpc.invoke.RpcInvoker;
import rpc.registry.RpcRegistry;
import rpc.registry.RpcService;
import rpc.registry.zookeeper.ServiceRegistry;
import serverStub.RpcProcessor;
import serverStub.ServerRpcInvoker;
import serverStub.ServerRpcProcessor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vincent on 2017/11/11.
 */
public class RpcServer implements ApplicationContextAware, InitializingBean{

    // Store the mapping between rpcInterface and it's implementation bean
    private Map<String, Object> handlerMap = new HashMap<>();

    @Getter @Setter private RpcProcessor processor;

    @Getter @Setter private RpcInvoker invoker;

    @Getter @Setter private RpcRegistry registry;

    public RpcServer() {
        processor = new ServerRpcProcessor();
        invoker = new ServerRpcInvoker(handlerMap);
        registry = new ServiceRegistry();
    }

    public void init() {
        invoker.setRegistry(registry);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        EventLoopGroup
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //Get all the SpringBean with "RpcService" annotation
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if(serviceBeanMap.size() > 0) {
            for(Object serviceBean : serviceBeanMap.values()) {
                String rpcInterface = serviceBean.getClass().getAnnotation(RpcService.class).value().getName();
                handlerMap.put(rpcInterface, serviceBean);
            }
        }
    }
}
