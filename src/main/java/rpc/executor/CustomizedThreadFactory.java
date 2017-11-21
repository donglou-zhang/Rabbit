package rpc.executor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A thread factory implementor which can be customized
 * It can achieve that using thread pool with your own named thread.
 *
 * @author Vincent
 * Created  on 2017/11/21.
 */
public class CustomizedThreadFactory implements ThreadFactory{

    private static final AtomicInteger threadNumber = new AtomicInteger(1);

    private final String name;

    private final boolean daemon;

    public CustomizedThreadFactory(String prefix) {
        this(prefix, false);
    }

    public CustomizedThreadFactory(String prefix, boolean daemon) {
        this.name = prefix + " -pool-thread-";
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, this.name + threadNumber.getAndIncrement());
        thread.setDaemon(this.daemon);
        return thread;
    }
}
