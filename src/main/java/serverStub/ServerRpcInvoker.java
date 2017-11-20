package serverStub;

import com.esotericsoftware.reflectasm.MethodAccess;
import common.exception.RpcException;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.invoke.RpcContext;
import rpc.invoke.RpcInvoker;
import rpc.protocol.model.RpcMessage;
import rpc.protocol.model.RpcMethod;
import rpc.registry.RpcRegistry;
import rpc.transmission.RpcConnector;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * ServerRpcInvoker takes charge of carrying out reflecting.
 * According to interface name, invoke the related method(bean).
 *
 * @author Vincent
 * Created  on 2017/11/16.
 */
public class ServerRpcInvoker implements RpcInvoker {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerRpcInvoker.class);

    @Getter @Setter private RpcRegistry registry;

    @Getter @Setter private Map<String, Object> handlerMap;

    public ServerRpcInvoker() {}

    public ServerRpcInvoker(Map<String, Object> map) {
        this.handlerMap = map;
    }

    /**
     * According to interface name, invoke the corresponding service bean to carry out
     * Return the response message.
     *
     * @param request
     * @return
     * @throws RpcException
     */
    @Override
    public RpcMessage invoke(RpcMessage request) throws RpcException {
        Class<?> rpcInterface = request.getBody().getRpcInterface();
        RpcMethod method = request.getBody().getRpcMethod();
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] params = method.getParameters();

        Object serviceBean = handlerMap.get(rpcInterface);
        if(serviceBean == null) {
            throw new RpcException("Can not find the corresponding service bean.");
        }

        try {
            RpcContext context = RpcContext.getRpcContext();
            context.setClientAddress(request.getClientAddress());
            context.setServerAddress(request.getServerAddress());
            context.setRpcAttachments(request.getRpcAttachments());

            LOGGER.debug("[Rabbit] Rpc server invoker is invoking, | rpcContext = {} |", context);

            MethodAccess methodAccess = MethodAccess.get(rpcInterface);
            int methodIndex = methodAccess.getIndex(methodName, parameterTypes);
            Object returnObject = methodAccess.invoke(serviceBean, methodIndex, params);
            return RpcMessage.newResponseMessage(request.getMid(), returnObject);
        } catch(Exception e) {
            LOGGER.warn("[Rabbit] Rpc server invoker error", e);
            if(isDeclaredException(e, rpcInterface, methodName, parameterTypes)) {
                return RpcMessage.newResponseMessage(request.getMid(), e);
            }else {
                throw new RpcException(RpcException.SERVER_ERROR, "server error");
            }
        } finally {
            RpcContext.removeRpcContext();
        }

    }

    /**
     * If the rpc interface is a kind of exception
     *
     * @param e
     * @param rpcInterface
     * @param methodName
     * @param parameterTypes
     * @return
     */
    private boolean isDeclaredException(Exception e, Class<?> rpcInterface, String methodName, Class<?>[] parameterTypes) {
        try {
            Method method = rpcInterface.getMethod(methodName, parameterTypes);
            Class<?>[] eTypes = method.getExceptionTypes();
            LOGGER.debug("[Rabbit] Rpc server invoker throw exception, | declaredExceptions = {}, thrownException = {} |", eTypes, e);
            for(Class<?> et : eTypes) {
                if(et.equals(e.getClass()))
                    return true;
            }
        }catch(Exception ex) {
            return false;
        }
        return false;
    }

    @Override
    public void setConnector(RpcConnector connector) {

    }
}
