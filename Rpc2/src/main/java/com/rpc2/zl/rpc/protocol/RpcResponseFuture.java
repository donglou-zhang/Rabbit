package com.rpc2.zl.rpc.protocol;

import com.rpc2.zl.rpc.exception.RpcException;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Vincent on 2018/7/12.
 */
public class RpcResponseFuture implements Future<Object> {

    private RpcRequest request;

    @Getter
    private RpcResponse response;

    private RpcResponseCallback responseCallback;

    @Getter
    private boolean isCancelled;

    private ReentrantLock lock = new ReentrantLock();

    private Condition doneCondition = lock.newCondition();

    public RpcResponseFuture(RpcRequest request) {
        this.request = request;
    }

    public Object getResultFromResponse() {
        if(isDone()) {
            return this.response.getResult();
        }
        throw new RpcException("Action is not completed");
    }

    public boolean isDone() {
        return this.response != null;
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        try {
            return this.get(3000,TimeUnit.MICROSECONDS);
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        long start = System.currentTimeMillis();
        if(!isDone()) {
            this.lock.lock();
            try {
                while(!isDone()) {
                    this.doneCondition.await(2000, TimeUnit.MILLISECONDS);
                    if(System.currentTimeMillis()-start > timeout) {
                        throw new RpcException("RpcResponseFuture.getResult() timeout");
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                this.lock.unlock();
            }
        }
        return this.getResultFromResponse();
    }

    /**
     * 请求完成并返回结果后，唤醒等待线程，并执行回调
     * @param response
     */
    public void executeAfterDone(RpcResponse response) {
        this.lock.lock();
        try{
            this.response = response;
            this.runCallback();
            this.doneCondition.signal();
        } finally {
            this.lock.unlock();
        }
    }

    public void setResponse(RpcResponse response) {
        this.lock.lock();
        try {
            this.response = response;
            this.doneCondition.signal();
        } finally {
            this.lock.unlock();
        }
    }

    public boolean cancel(boolean mayInterrupteIfRunning) {
        if(!mayInterrupteIfRunning)
            return false;
        RpcResponse errorResult = new RpcResponse();
        errorResult.setRequestId(this.request.getRequestId());
        errorResult.setResult("request has been cancelled");
        response = errorResult;
        this.isCancelled = true;
        return true;
    }

    private void runCallback() {
        if(this.responseCallback == null) {
            return;
        }

        if(isDone()) {
            if(this.response.getError() == null) {
                responseCallback.onSuccess(this.response);
            } else {
                responseCallback.onException(new RpcException(new RuntimeException(this.response.getError())));
            }
        }
    }

    public void setResponseCallback(RpcResponseCallback callback) {
        this.responseCallback = callback;
        if(this.isDone()) {
            this.runCallback();
        }
    }
}
