package com.rabbit.zl.rpc.registry.zookeeper;

import lombok.NonNull;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbit.zl.rpc.registry.RpcDiscovery;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Discover and get the registry service
 *
 * @author Vincent
 * Created  on 2017/11/12.
 */
public class ServiceDiscovery implements RpcDiscovery{

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceDiscovery.class);

    private volatile Map<String, List<String>> serviceMap = new HashMap<>();

    private IZkChildListener zkChildListener = new ZkChildListener();

    private Map<String, IZkChildListener> serviceListeners = new ConcurrentHashMap<>();

    private ZkClient zkClient;

    private String registerAddr;

    private boolean isInitialized = false;

    public ServiceDiscovery(String host, int port) {
        this.registerAddr = host + ":" + String.valueOf(port);
        init();
    }

    private void init() {
        if(isInitialized) {
            return;
        }

        isInitialized = true;
        zkClient = new ZkClient(this.registerAddr);

        LOGGER.info("Connect to zookeeper server: [{}]", this.registerAddr);

        zkClient.subscribeStateChanges(new IZkStateListener() {
            @Override
            public void handleStateChanged(Watcher.Event.KeeperState keeperState) throws Exception {
                monitorNode(zkClient);
            }

            @Override
            public void handleNewSession() throws Exception {
                monitorNode(zkClient);
            }
        });
    }



    /**
     * Monitor the zookeeper node to update service data on time
     *
     * @param zkClient
     */
    private void monitorNode(@NonNull final ZkClient zkClient) {
        //TODO need to get the application
        String application = "";

        String path = Constant.ZK_REGISTRY_PATH + "/" + application;

        List<String> serviceList = zkClient.getChildren(path);

        if(serviceList.isEmpty()) {
            LOGGER.warn("Can not find any address node on path: [{}]. Please check your zookeeper service :( \n", path);
        } else {
            for(String service : serviceList) {
                List<String> nodes = this.discoverAll(application, service);
                if(nodes != null && !nodes.isEmpty()) {
                    serviceMap.put(path+"/"+service, nodes);
                }
            }

            // updated node list
            if(!serviceMap.values().isEmpty()) {
                LOGGER.debug("Update node list: {}", serviceMap.values().stream().flatMap(Collection::stream).distinct().collect(Collectors.toList()));
            }
        }
    }

    @Override
    public String discover(String service) {
        return null;
    }

    @Override
    public String discover(String application, String service) {
        //TODO load balancing according to weight

        return null;
    }

    @Override
    public List<String> discoverAll(String service) {
        return null;
    }

    @Override
    public List<String> discoverAll(String application, String rpcInterface) {
        String servicePath = Constant.ZK_REGISTRY_PATH + "/" + application + "/" + rpcInterface;

        List<String> children = null;
        if(zkClient.exists(servicePath)) {
            //All the children's path don't contain the prefix path(application/rpcInterface)
            children = zkClient.getChildren(servicePath);
        }

        if(!serviceListeners.containsKey(servicePath)) {
            serviceListeners.put(servicePath, zkChildListener);
            // For each service path(application/rpcInterface), monitor it's children changes.
            zkClient.subscribeChildChanges(servicePath, zkChildListener);
        }
        return children;
    }

    class ZkChildListener implements IZkChildListener {

        @Override
        public void handleChildChange(String parentPath, List<String> currentChildren) throws Exception {
            if(currentChildren != null && !currentChildren.isEmpty()) {
                monitorNode(zkClient);
            }
        }
    }

}
