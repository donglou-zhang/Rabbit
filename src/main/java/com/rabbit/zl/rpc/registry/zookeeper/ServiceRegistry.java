package com.rabbit.zl.rpc.registry.zookeeper;

import com.rabbit.zl.common.exception.RpcException;
import com.rabbit.zl.rpc.registry.RpcRegistry;
import lombok.Getter;
import lombok.Setter;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.rabbit.zl.rpc.registry.RpcRegistryService;


/**
 * Use zookeeper to implement service registry
 *
 * @author Vincent
 * Created  on 2017/11/12.
 */
@Component("serviceRegistry")
public class ServiceRegistry implements RpcRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistry.class);

    @Getter @Setter private ZkClient zkClient;

    @Getter @Setter private String registerHost;

    @Getter @Setter private int registerPort;

    public ServiceRegistry(String registryHost, int registryPort) {
        this.zkClient =  new ZkClient(registryHost + ":" + String.valueOf(registryPort));
        System.out.println("zookeeper connect: " + registryHost+":"+registryPort);
    }

    /**
     *
     * @param service Need to be registered
     */
    @Override
    public void register(RpcRegistryService service) {
        if(service == null) {
            throw new RpcException("Registry service is null, please check!");
        }
        removeNode(service);
        createNode(service);
    }

    @Override
    public void unregister(RpcRegistryService service) {
        if(service == null) {
            throw new RpcException("Registry service is null, please check!");
        }
        removeNode(service);
    }

    private void createNode(RpcRegistryService service) {

        String persistentPath = getRegistryPath(service);

        if(!zkClient.exists(persistentPath)) {
            zkClient.createPersistent(persistentPath, true);
        }

        System.out.println("Create persistent node [ " + persistentPath + "]");
//        LOGGER.debug("Create persistent node [{}]", path);

        String tempServiceNode = persistentPath + Constant.PATH_SEPERATOR + getServerAddress(service);

        System.out.println("Create ephemeral service node [ " + tempServiceNode + "]");
        zkClient.createEphemeral(tempServiceNode, String.valueOf(service.getWeight()).getBytes());
//        LOGGER.debug("Create ephemeral service node [{}]", serviceNode);
    }

    private void removeNode(RpcRegistryService service) {
        String servicePath = getRegistryPath(service);
        if(!zkClient.exists(servicePath)) {
            if(!zkClient.delete(servicePath)) {
                LOGGER.warn("Delete service node [{}] fail.", servicePath);
            }
        }
    }

    private String getRegistryPath(RpcRegistryService service) {
        String application = service.getApplication();
        String rpcInterface = service.getRpcInterface();
        String version = service.getVersion();

        return Constant.ZK_DATA_PATH + Constant.PATH_SEPERATOR + application + Constant.PATH_SEPERATOR
                + rpcInterface + Constant.PATH_SEPERATOR + version;
    }

    private String getServerAddress(RpcRegistryService service) {
        String serverHost = service.getServerHost();
        int serverPort = service.getServerPort();
        return serverHost + ":" + String.valueOf(serverPort);
    }
}
