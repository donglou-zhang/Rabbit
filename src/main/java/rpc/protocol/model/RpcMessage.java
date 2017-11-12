package rpc.protocol.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * RPC messages use generic message format for transferring data.
 * <pre>
 *      rpc-message = rpc-header + [ rpc-body ]
 *      rpc-body is optional
 * </pre>
 *
 * @author Vincent
 * Created  on 2017/11/10.
 */
@ToString
@EqualsAndHashCode(of = {"header", "body"})
public class RpcMessage implements Serializable{

    private static final long serialVersionUID = 5138100956693144357L;

    @Getter @Setter private RpcHeader header;

    @Getter @Setter private RpcBody body;

    public boolean isOneWay() {
        return header.isOw();
    }

    public void setOneWay(boolean oneway) {
        if(oneway)
            header.setOw();
    }

    public boolean isResponse() {
        return header.isRp();
    }

    public void setResponse(boolean response) {
        if(response)
            header.setRp();
    }

    public boolean isHeartBeat() {
        return header.isHb();
    }

    public void setHeartBeat(boolean heartBeat) {
        if(heartBeat)
            header.setHb();
    }

    public long getMid() {
        return header.getMid();
    }

    public void setMid(long id) {
        header.setMid(id);
    }

    public Exception getException() {
        return body.getRpcException();
    }

    public void setException(Exception e) {
        body.setRpcException(e);
    }

    public int getRpcTimeoutInMillis() {
        return body.getRpcOption().getRpcTimeoutInMillis();
    }

    public void setRpcTimeoutInMillis(int rpcTimeoutInMillis) {
        body.getRpcOption().setRpcTimeoutInMillis(rpcTimeoutInMillis);
    }
}
