package server;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import rpc.executor.ServerRpcExecutorFactory;
import rpc.invoke.RpcInvoker;
import rpc.registry.RpcRegistry;
import rpc.registry.RpcService;
import rpc.registry.zookeeper.ServiceRegistry;
import rpc.transmission.RpcAcceptor;
import serverStub.RpcProcessor;
import serverStub.ServerRpcInvoker;
import serverStub.ServerRpcProcessor;
import transfer.netty.NettyServerAcceptor;

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

    @Getter @Setter private RpcRegistry registry;

    @Getter @Setter private ServerRpcExecutorFactory executorFactory;

    public RpcServer() {
        processor = new ServerRpcProcessor();
        invoker = new ServerRpcInvoker();
        registry = new ServiceRegistry();
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

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //Get all the SpringBean with "RpcService" annotation
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if(serviceBeanMap.size() > 0) {
            for(Object serviceBean : serviceBeanMap.values()) {
                String rpcInterface = serviceBean.getClass().getAnnotation(RpcService.class).value().getName();
                serviceBeanMap.put(rpcInterface, serviceBean);
            }
        }
    }
}
