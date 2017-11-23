package com.rabbit.zl.rpc.protocol.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.net.InetSocketAddress;

/**
 * A <code>RpcOption<code/> provides optional information about rpc invocation
 *
 * @author Vincent
 * Created  on 2017/11/12.
 */
@ToString
public class RpcOption implements Serializable {

    private static final long serialVersionUID = -9035174922898235358L;

    @Getter @Setter transient private InetSocketAddress serverAddress;

    @Getter @Setter transient private InetSocketAddress clientAddress;

    @Getter @Setter private int rpcTimeoutInMillis = Integer.MAX_VALUE;
}
