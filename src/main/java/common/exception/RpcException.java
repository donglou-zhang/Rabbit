package common.exception;

import lombok.ToString;
import rpc.registry.RpcRegistry;

/**
 * Rpc exception
 *
 * @author Vincent
 * Created  on 2017/11/12.
 */
@ToString(callSuper = true)
public final class RpcException extends RuntimeException{


    private static final long serialVersionUID = -4168884981656035910L;

    public RpcException(String message) {
        super(message);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(Throwable cause) {
        super(cause);
    }
}
