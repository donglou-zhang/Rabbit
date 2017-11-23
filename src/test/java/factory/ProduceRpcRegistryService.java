package factory;

import com.rabbit.zl.rpc.registry.RpcRegistryService;

/**
 * Test for producing {@link RpcRegistryService} instance
 *
 * @author Vincent
 * Created  on 2017/11/22.
 */
public class ProduceRpcRegistryService {

    public static RpcRegistryService getRpcRegistryService() {
        RpcRegistryService service = new RpcRegistryService();
        service.setApplication("TestApp");
        service.setRpcInterface("TestService");
        service.setMethodName("testFunc");
        service.setVersion("1.0.0");
        service.setWeight(1);
        service.setServerHost("127.0.0.1");
        service.setServerPort(8001);
        return service;
    }
}
