package rpc.monitor;

import java.util.concurrent.ExecutorService;

/**
 * An {@link ExecutorService} that provides extra monitoring function
 *
 * @author Vincent
 * Created  on 2017/11/20.
 */
public interface MonitoringExecutorService extends ExecutorService {

    /**
     * @return the approximate waiting task count
     */
    int getWaitingTaskCount();

    /**
     * @return the approximate current executing task count
     */
    int getExecutingTaskCount();

    /**
     * @return the approximate completed task count
     */
    int getCompleTaskCount();
}
