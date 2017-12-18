package com.rabbit.zl.rpc.registry.zookeeper;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbit.zl.rpc.registry.RpcDiscovery;
import org.springframework.stereotype.Component;

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

    @Getter @Setter private ZkClient zkClient;

    @Getter @Setter private String registerHost;

    @Getter @Setter private int registerPort;

    @Getter @Setter private String defaultApplication;

    public ServiceDiscovery(String registryHost, int registryPort, String defaultApplication) {
            this.registerHost = registryHost;
            this.registerPort = registryPort;
            this.defaultApplication = defaultApplication;
            init();
    }

    private void init() {
        this.zkClient = new ZkClient(registerHost, registerPort);

        System.out.println("ServiceDiscovery: connect to zookeeper [" + registerHost + ":" + registerPort + "]");

        LOGGER.info("Connect to zookeeper server: [{}]", (this.registerHost + ":" + this.registerPort));

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

        String path = Constant.ZK_DATA_PATH;

        List<String> serviceList = zkClient.getChildren(path);

        if(serviceList.isEmpty()) {
            LOGGER.warn("Can not find any address node on path: [{}]. Please check your zookeeper service :( \n", path);
        } else {
            for(String service : serviceList) {
                List<String> nodes = this.discoverAll(defaultApplication, service);
                if(nodes != null && !nodes.isEmpty()) {
                    serviceMap.put(path + Constant.PATH_SEPERATOR + defaultApplication, nodes);
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
        return discoverAll(this.defaultApplication, service);
    }

    @Override
    public List<String> discoverAll(String application, String rpcInterface) {
        String servicePath = Constant.ZK_DATA_PATH + Constant.PATH_SEPERATOR + application;

        List<String> result = new ArrayList<>();
        if(zkClient.exists(servicePath)) {
            //All the children's path don't contain the prefix path(application/rpcInterface)
            List<String> children = zkClient.getChildren(servicePath);
            int size = children.size();
            for(int i=0; i<size; i++) {
                String fullPersistentPath = servicePath + Constant.PATH_SEPERATOR + children.get(i);
                List<String> ephemeralChildren = zkClient.getChildren(fullPersistentPath);
                for(String ephemeralInfo : ephemeralChildren) {
                    String fullPath = fullPersistentPath + Constant.PATH_SEPERATOR + ephemeralInfo;
                    int weight = zkClient.readData(fullPath);
                    result.add(fullPath + Constant.PATH_SEPERATOR + weight);
                }
            }
        }

        if(!serviceListeners.containsKey(servicePath)) {
            serviceListeners.put(servicePath, zkChildListener);
            // For each service path(application/rpcInterface), monitor it's children changes.
            zkClient.subscribeChildChanges(servicePath, zkChildListener);
        }
        // /path/version&host:port/weight
        return result;
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
