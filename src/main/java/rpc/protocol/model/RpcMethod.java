package rpc.protocol.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Rpc called method, contains method name, parameterTypes and parameters
 *
 * @author Vincent
 * Created  on 2017/11/12.
 */
@ToString
@EqualsAndHashCode(of = {"name", "parameterTypes"})
public class RpcMethod implements Serializable{

    private static final long serialVersionUID = -4302065109637231162L;

    @Getter @Setter private String name;

    @Getter  private Class<?>[] parameterTypes;

    @Getter private Object[] parameters;

    public RpcMethod() {}

    public RpcMethod(String name, Class<?>[] parameterTypes) {
        this.name = name;
        this.parameterTypes = parameterTypes;
    }

    public RpcMethod(String name, Class<?>[] parameterTypes, Object[] parameters) {
        this(name, parameterTypes);
        this.parameters = parameters;
    }

    public void setParameterTypes(Class<?>... parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public void setParameters(Object... parameters) {
        this.parameters = parameters;
    }
}
