package rpc.registry;

import java.util.List;

public interface RpcDiscovery {

    String discover(String rpcInterface);

    String discover(String application, String rpcInterface);

    List<String> discoverAll(String rpcInterface);

    List<String> discoverAll(String application, String rpcInterface);
}
