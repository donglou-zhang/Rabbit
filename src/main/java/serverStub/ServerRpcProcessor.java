package serverStub;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.executor.RpcExecutorFactory;
import rpc.invoke.RpcInvoker;
import rpc.protocol.model.RpcMessage;

public class ServerRpcProcessor implements RpcProcessor{

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerRpcProcessor.class);

    @Getter @Setter private RpcInvoker invoker;

    private RpcExecutorFactory executorFactory;

    @Override
    public void process(RpcMessage request) {

    }
}
