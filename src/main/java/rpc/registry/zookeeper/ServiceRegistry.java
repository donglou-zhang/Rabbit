package rpc.registry.zookeeper;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;


/**
 * Use zookeeper to implement service registry
 *
 * @author Vincent
 * Created  on 2017/11/12.
 */
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
                createNode(zk, service);
            }
        }
    }

    private ZooKeeper connectZookeeper() {
        ZooKeeper zk = null;
        try{
            String registryAddress = registerHost + ":" + String.valueOf(registerPort);
            zk = new ZooKeeper(registryAddress, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if(watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                        // make the blocked thread continue executing
                        latch.countDown();
                    }
                }
            });
            // block current thread
            latch.await();
        }catch(IOException | InterruptedException e) {
            LOGGER.error("", e);
        }
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
