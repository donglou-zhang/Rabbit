package serverStub;

import common.exception.RpcException;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.executor.RpcExecutorFactory;
import rpc.invoke.RpcInvoker;
import rpc.monitor.MonitoringExecutorService;
import rpc.protocol.model.RpcMessage;
import rpc.transmission.RpcChannel;

import java.util.concurrent.*;

/**
 * ServerRpcProcessor takes charge of dispatching thread to handle request
 *
 * @author Vincent
 * Created  on 2017/11/18.
 */
public class ServerRpcProcessor implements RpcProcessor{

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerRpcProcessor.class);

    @Getter @Setter private RpcInvoker invoker;

    @Getter @Setter private RpcExecutorFactory executorFactory;

    @Getter @Setter private ExecutorService timeoutExecutor;

    private RpcMessage response;

    public ServerRpcProcessor() {}

    @Override
    public RpcMessage process(RpcMessage request, RpcChannel channel) {
        MonitoringExecutorService executor = null;
        //TODO check getRpcInterface().getName() is right or not
        executor = executorFactory.getMonitorExecutor(request.getApplication() + "/" + request.getRpcInterface().getName());
        executor.execute(new ProcessTask(request, channel));
        LOGGER.debug("Server processor dispatch thread to handle task");
        return response;
    }

    private RpcMessage handleMessage(RpcMessage request) {
        RpcMessage response = null;
        try {
            response = invoker.invoke(request);
        } catch(RpcException e) {
            response = RpcMessage.newResponseMessage(request.getMid(), e);
        }
        return response;
    }

    private int getRpcTimeoutInMillis(RpcMessage request) {
        return (request.getRpcTimeoutInMillis()==0)?Integer.MAX_VALUE:request.getRpcTimeoutInMillis();
    }

    class ProcessTask implements Runnable {

        private RpcMessage request;

        private RpcChannel channel;

        public ProcessTask(RpcMessage request, RpcChannel channel) {
            this.request = request;
            this.channel = channel;
        }

        @Override
        public void run() {
            try {
                Future<RpcMessage> future = timeoutExecutor.submit(new Callable<RpcMessage>() {
                    @Override
                    public RpcMessage call() throws Exception {
                        return handleMessage(request);
                    }
                });

                if(request.isOneWay()) return;

                response = future.get(getRpcTimeoutInMillis(request), TimeUnit.MICROSECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                response = RpcMessage.newResponseMessage(request.getMid(), new RpcException(RpcException.UNKNOWN, "unknown server error"));
            } catch (ExecutionException e) {
                e.printStackTrace();
                response = RpcMessage.newResponseMessage(request.getMid(), new RpcException(RpcException.SERVER_ERROR, "server error"));
            } catch (TimeoutException e) {
                e.printStackTrace();
                response = RpcMessage.newResponseMessage(request.getMid(), new RpcException(RpcException.SERVER_TIMEOUT, "server timeout"));
            }

        }
    }
}
