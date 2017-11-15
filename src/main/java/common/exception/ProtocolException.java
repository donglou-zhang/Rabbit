package common.exception;

/**
 * @author Vincent
 * Created  on 2017/11/12.
 */
public class ProtocolException extends RuntimeException {


    public ProtocolException() {
        super();
    }

    public ProtocolException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProtocolException(String message) {
        super(message);
    }

    public ProtocolException(Throwable cause) {
        super(cause);
    }
}
