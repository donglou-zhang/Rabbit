package com.rabbit.zl.rpc.protocol.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * Represents a RPC body field.
 * <p>
 * A body that can be sent or received with a rpc message,
 * but not all messages contain a body, it is optional.
 * The body contains a block of arbitrary data and can be serialized by specific serializer.
 *
 * @author Vincent
 * Created  on 2017/11/10.
 */
@ToString
@EqualsAndHashCode(of = {"rpcInterface", "rpcMethod", "rpcReturn", "rpcException", "rpcAttachments"})
public class RpcBody implements Serializable{

    private static final long serialVersionUID = -2481171914209936374L;

    @Getter @Setter private String rpcId;

    @Getter @Setter private String application;

    @Getter @Setter private Class<?> rpcInterface;

    @Getter @Setter private RpcMethod rpcMethod;

    @Getter @Setter private RpcOption rpcOption;

    @Getter @Setter private Map<String, String> rpcAttachments;

    @Getter @Setter private Object rpcReturn;

    @Getter @Setter private Exception rpcException;
}
