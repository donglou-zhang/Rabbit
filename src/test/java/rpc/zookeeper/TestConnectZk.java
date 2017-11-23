package rpc.zookeeper;

import com.rabbit.zl.common.test.CaseCounter;
import factory.ProduceRpcRegistryService;
import org.I0Itec.zkclient.ZkClient;
import org.junit.Test;
import com.rabbit.zl.rpc.registry.RpcRegistryService;
import com.rabbit.zl.rpc.registry.zookeeper.Constant;
import com.rabbit.zl.rpc.registry.zookeeper.ServiceRegistry;

/**
 * Test for connecting zookeeper
 *
 * @author Vincent
 * Created  on 2017/11/22.
 */
public class TestConnectZk {

    @Test
    public void testConnectZk() {
        ZkClient zkClient = new ServiceRegistry("127.0.0.1", 2181).getZkClient();
        if(!zkClient.exists(Constant.ZK_DATA_PATH)) {
            zkClient.createPersistent(Constant.ZK_DATA_PATH, true);
        }
        if(zkClient.exists(Constant.ZK_DATA_PATH)) {
            System.out.println("path [ " + Constant.ZK_DATA_PATH + " ] exists");
        }
//        zkClient.createPersistent("/test");
//        zkClient.createPersistent("/test/a1", "这是测试内容");
//        System.out.println((String)zkClient.readData("/test/a1"));
        System.out.println(String.format("[Rabbit] (^_^) <%s> Test Case Success -> connect zookeeper. ", CaseCounter.incr(1)));
    }

    @Test
    public void testCreateRegistryNode() {
        ServiceRegistry registry = new ServiceRegistry("127.0.0.1", 2181);
        RpcRegistryService service = ProduceRpcRegistryService.getRpcRegistryService();
        registry.register(service);
        System.out.println(String.format("[Rabbit] (^_^) <%s> Test Case Success -> zookeeper register service. ", CaseCounter.incr(1)));
    }
}
