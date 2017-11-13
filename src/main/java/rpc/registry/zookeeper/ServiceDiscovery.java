package rpc.registry.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Discover and get the registry service
 *
 * @author Vincent
 * Created  on 2017/11/12.
 */
public class ServiceDiscovery {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceDiscovery.class);

    private CountDownLatch latch = new CountDownLatch(1);

    private volatile List<String> dataList = new ArrayList<>();

    private String registerHost;

    public ServiceDiscovery(String host, int port) {
        this.registerHost = host;
        this.registerPort = port;

        ZooKeeper zk = connectServer();
        if(zk != null) {
            monitorNode(zk);
        }
    }

    private ZooKeeper connectServer() {
        ZooKeeper zk = null;
        try {
            String registryAddress = registerHost + ":" + String.valueOf(registerPort);
            zk = new ZooKeeper(registryAddress, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if(watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }
                }
            });
            latch.await();
        }catch(IOException | InterruptedException e) {
            LOGGER.error("", e);
        }
        return zk;
    }

    /**
     * Monitor the zookeeper node to update service data on time
     *
     * @param zk
     */
    private void monitorNode(final ZooKeeper zk) {
        try {
            List<String> nodeList = zk.getChildren(Constant.ZK_REGISTRY_PATH, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if(watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                        monitorNode(zk);
                    }
                }
            });
            List<String> dataList = new ArrayList<>();
            for(String node : nodeList) {
                byte[] bytes = zk.getData(Constant.ZK_REGISTRY_PATH + "/" + node, false, null);
                dataList.add(new String(bytes));
            }
            LOGGER.debug("node data : {}", dataList);
            this.dataList = dataList;
        }catch(KeeperException | InterruptedException e) {
            LOGGER.error("", e);
        }
    }

    /**
     * Get the data from zookeeper
     *
     * @return
     */
    public String discover() {
        String data = null;
        int size = this.dataList.size();

        //TODO can do "load balancing"
        if(size == 1) {
            data = this.dataList.get(0);
            LOGGER.debug("Using only data : {}", data);
        }else {
            data = this.dataList.get(ThreadLocalRandom.current().nextInt(size));
            LOGGER.debug("Using random data : {}", data);
        }
        return data;
    }

    private int registerPort;
}
