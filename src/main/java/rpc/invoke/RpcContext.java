package rpc.invoke;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Rpc context is a thread local context
 * Each rpc invocation bind a rpc context instance to current thread
 * final class means it can't be inherited
 *
 * @author Vincent
 * Created  on 2017/11/13.
 */
@ToString
public final class RpcContext {

    @Getter @Setter private InetSocketAddress serverAddress;
    @Getter @Setter private InetSocketAddress clientAddress;
    @Getter @Setter private Map<String, String> rpcAttachments;
    @Getter @Setter private String rpcId;
    @Getter @Setter private int rpcTimeoutInMillis;
    @Getter @Setter private boolean oneWay;
    @Getter @Setter private boolean async;
            @Setter private Future<?> future;

    /**
     * ThreadLocal provides a "thread-level" variable scope
     * Can get easily in the same thread
     */
    private static final ThreadLocal<RpcContext> THREAD_LOCAL = new ThreadLocal<RpcContext>() {
        @Override
        protected RpcContext initialValue() {
            return new RpcContext();
        }
    };

    public RpcContext() {
        rpcAttachments = new HashMap<>();
    }

    /**
     * Get rpc context
     *
     * @return
     */
    public static RpcContext getRpcContext() {
        return THREAD_LOCAL.get();
    }

    public static void removeRpcContext() {
        THREAD_LOCAL.remove();
    }

    /**
     * Add attachments element
     *
     * @param key
     * @param value
     * @return
     */
    public RpcContext setRpcAttachment(String key, String value) {
        this.rpcAttachments.put(key, value);
        return this;
    }

    /**
     * Remove attachments element
     *
     * @param key
     * @return
     */
    public RpcContext removeRpcAttachment(String key) {
        this.rpcAttachments.remove(key);
        return this;
    }

    public String getRpcAttachment(String key) {
        return this.rpcAttachments.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> Future<T> getFuture() {
        return (Future<T>) future;
    }
}
