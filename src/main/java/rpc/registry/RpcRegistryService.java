package rpc.registry;

import lombok.Getter;
import lombok.Setter;

/**
 * Service registered in the registry server
 * ZK_REGISTRY_PATH / application / rpcInterface / method / version / weight / serverHost:serverPort
 *
 * @author Vincent
 * Created  on 2017/11/12.
 */
public class RpcRegistryService {

    @Getter @Setter private String application;

    @Getter @Setter private String rpcInterface;

    @Getter @Setter private String methodName;

    @Getter @Setter private String version;

    @Getter @Setter private String serverHost;

    @Getter @Setter private int serverPort;

    @Getter @Setter private int weight;

    public RpcRegistryService() {}

    public RpcRegistryService(String application, String rpcInterface, String methodName, String
                              version, String serverHost, int serverPort, int weight) {
        this.application = application;
        this.rpcInterface = rpcInterface;
        this.methodName = methodName;
        this.version = version;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.weight = weight;
    }

}
