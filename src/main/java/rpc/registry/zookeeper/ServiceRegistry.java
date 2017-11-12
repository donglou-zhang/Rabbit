package rpc.registry.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;


public class ServiceRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistry.class);

    //assist achieving synchronized
    private CountDownLatch latch = new CountDownLatch(1);

    private String registerHost;

    private int registerPort;

    /**
     * register service at the machine with (host, port)url, like zk(127.0.0.1:2181)
     * @param host
     * @param port
     */
    public ServiceRegistry(String host, int port) {
        this.registerHost = host;
        this.registerPort = port;
    }

    /**
     * register service
     * @param service include the info of registered service
     */
    public void register(byte[] service) {
        if(service != null) {
            ZooKeeper zk = connectZookeeper();
            if(zk != null) {

            }
        }
    }

    private ZooKeeper connectZookeeper() {
        ZooKeeper zk = null;
        return zk;
    }

    private void createNode(ZooKeeper zk, byte[] data) {
        try {
            String path = zk.create(Constant.ZK_REGISTRY_PATH, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            LOGGER.debug("create zookeeper node ({} => {})", path, data);
        }catch(KeeperException | InterruptedException e) {
            LOGGER.error("", e);
        }
    }
}
