package me.zhixingye.im.sdk.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import me.zhixingye.im.constant.ResponseCode;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.BasicService;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
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
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
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
