package me.zhixingye.im.sdk.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import me.zhixingye.im.constant.ResponseCode;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.BasicService;

/**
 * Created by zhixingye on 2020年05月01日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class ProxyHelper {

    @SuppressWarnings("unchecked")
    public static <T extends BasicService> T createProxy(final T instance) {
        return (T) Proxy.newProxyInstance(
                instance.getClass().getClassLoader(),
                instance.getClass().getInterfaces(),
                new NullStringHandler(instance));
    }

    public static void callRemoteFail(RequestCallback<?> callback) {
        if (callback != null) {
            callback.onFailure(
                    ResponseCode.INTERNAL_IPC_EXCEPTION.getCode(),
                    ResponseCode.INTERNAL_IPC_EXCEPTION.getMsg());
        }
    }


    /**
     * protoBuf要求参数不能为null，包括String，所以这里统一用动态代理转换
     */
    private static class NullStringHandler implements InvocationHandler {

        private Object mOriginal;

        NullStringHandler(Object Original) {
            mOriginal = Original;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (args != null && args.length != 0) {
                Class[] paramsClass = method.getParameterTypes();
                for (int i = 0, length = paramsClass.length; i < length; i++) {
                    if (paramsClass[i] == String.class && args[i] == null) {
                        args[i] = "";
                    }
                }
            }
            return method.invoke(mOriginal, args);
        }
    }

}
