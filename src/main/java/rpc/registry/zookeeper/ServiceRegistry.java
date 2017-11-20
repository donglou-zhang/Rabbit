package rpc.registry.zookeeper;

import common.exception.RpcException;
import lombok.Getter;
import lombok.Setter;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.registry.RpcRegistry;
import rpc.registry.RpcRegistryService;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;


/**
 * Use zookeeper to implement service registry
 *
 * @author Vincent
 * Created  on 2017/11/12.
 */
public class ServiceRegistry implements RpcRegistry{

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistry.class);

    private ZkClient zkClient;

    @Getter @Setter private String registerHost;

    @Getter @Setter private int registerPort;

    public ServiceRegistry() {}

    /**
     * register service at the machine with (host, port)url, like zk(127.0.0.1:2181)
     * @param host
     * @param port
     */
    public ServiceRegistry(String host, int port) {
        String zkServerAddr = host + ":" + String.valueOf(port);
        zkClient = new ZkClient(zkServerAddr);
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
        String application = service.getApplication();
        String rpcInterface = service.getRpcInterface();

        String path = Constant.ZK_DATA_PATH + "/" + application + "/" + rpcInterface;

        if(!zkClient.exists(path)) {
            zkClient.createPersistent(path, true);
        }

        LOGGER.debug("Create persistent node [{}]", path);

        String serviceNode = Constant.ZK_DATA_PATH + "/" + getFullServicePath(service);

        zkClient.createEphemeral(serviceNode, "".getBytes());
        LOGGER.debug("Create ephemeral service node [{}]", serviceNode);
    }

    private void removeNode(RpcRegistryService service) {
        String servicePath = Constant.ZK_DATA_PATH + "/" + getFullServicePath(service);
        if(!zkClient.exists(servicePath)) {
            if(!zkClient.delete(servicePath)) {
                LOGGER.warn("Delete service node [{}] fail.", servicePath);
            }
        }
    }

    private String getFullServicePath(RpcRegistryService service) {
        String application = service.getApplication();
        String rpcInterface = service.getRpcInterface();
        String method = service.getMethodName();
        String version = service.getVersion();
        int weight = service.getWeight();
        String serverHost = service.getServerHost();
        int serverPort = service.getServerPort();

        return application + "/" + rpcInterface + "/" + method + "/" + version + "/" + String.valueOf(weight)
                + "/" + serverHost + ":" + String.valueOf(serverPort);
    }
}
