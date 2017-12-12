package com.rabbit.zl.serverStub;

import com.rabbit.zl.common.exception.RpcException;
import com.rabbit.zl.rpc.executor.CustomizedThreadFactory;
import com.rabbit.zl.rpc.executor.ServerRpcExecutorFactory;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbit.zl.rpc.executor.RpcExecutorFactory;
import com.rabbit.zl.rpc.invoke.RpcInvoker;
import com.rabbit.zl.rpc.monitor.MonitoringExecutorService;
import com.rabbit.zl.rpc.protocol.model.RpcMessage;
import com.rabbit.zl.rpc.transmission.RpcChannel;

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

    private Object lock = new Object();

    public ServerRpcProcessor() {
        executorFactory = new ServerRpcExecutorFactory();
//        timeoutExecutor = Executors.newFixedThreadPool(3);
        timeoutExecutor = Executors.newCachedThreadPool(new CustomizedThreadFactory("Rabbit-timeout"));
    }

    @Override
    public RpcMessage process(RpcMessage request, RpcChannel channel) {
        if(request == null)
            throw new RpcException("Received request message is null, should not dispatch thread to process");
        //TODO
        MonitoringExecutorService executor;
        executor = executorFactory.getMonitorExecutor(request.getApplication() + "/" + request.getRpcInterface().getName());

//        ExecutorService executor = Executors.newFixedThreadPool(2);

        LOGGER.debug("Server processor begin to dispatch a thread to handle task...");
        executor.execute(new ProcessTask(request, channel));

        //wait the handle thread to execute result
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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

                synchronized (lock) {
                    lock.notifyAll();
                }

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
