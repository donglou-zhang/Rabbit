package client;

import rpc.registry.zookeeper.ServiceDiscovery;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Rpc proxy for creating rpc service proxy
 * Through passing in interface name to carry out rpc call, just like local call
 *
 * @author Vincent
 * Created  on 2017/11/11.
 */
public class RpcProxy {

    private String remoteHost;

    private int remotePort;

    private ServiceDiscovery serviceDiscovery;

    public RpcProxy(String remoteHost, int remotePort) {
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    @SuppressWarnings(("unchecked"))
    public <T>T getBean(Class<?> interfaceClass) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass}, new StandardInvocation());
    }

    class StandardInvocation implements InvocationHandler {

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return null;
        }
    }
}
