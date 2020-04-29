package me.zhixingye.im.sdk.proxy;


import com.google.protobuf.GeneratedMessageLite;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;

import me.zhixingye.im.constant.ResponseCode;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.IRemoteService;
import me.zhixingye.im.sdk.IResultCallback;
import me.zhixingye.im.tool.Logger;

/**
 * Created by zhixingye on 2020年02月02日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public abstract class BasicProxy {

    private IMServiceConnector mConnectProxy;

    BasicProxy(IMServiceConnector proxy) {
        if (proxy == null) {
            throw new NullPointerException("proxy == null");
        }
        mConnectProxy = proxy;
    }

    void connectIMServiceIfNeed(ConnectCallback callback) {
        mConnectProxy.connectRemoteIMService(callback);
    }

    protected abstract void onConnectRemoteService(IRemoteService service);

    @SuppressWarnings("unchecked")
    public static <T extends BasicProxy> T createProxy(T instance) {
        return (T) Proxy.newProxyInstance(
                instance.getClass().getClassLoader(),
                new Class[]{instance.getClass()},
                (proxy, method, args) -> {
                    if (method.getReturnType() != void.class) {
                        return method.invoke(instance, args);
                    }

                    instance.connectIMServiceIfNeed(new ConnectCallback() {
                        @Override
                        public void onCompleted(IRemoteService service) {
                            instance.onConnectRemoteService(service);
                            try {
                                method.invoke(instance, args);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            t.printStackTrace();
                            for (int i = args.length - 1; i >= 0; i--) {
                                if (args[i] instanceof RequestCallback) {
                                    ((RequestCallback) args[i]).onFailure(
                                            ResponseCode.INTERNAL_IPC_EXCEPTION.getCode(),
                                            ResponseCode.INTERNAL_IPC_EXCEPTION.getMsg());
                                }
                            }
                        }
                    });
                    return null;
                });
    }

    protected static class ResultCallbackWrapper<T> extends IResultCallback.Stub {

        private static final String TAG = "ResultCallbackWrapper";

        private RequestCallback<T> mCallback;

        ResultCallbackWrapper(RequestCallback<T> callback) {
            mCallback = callback;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onCompleted(byte[] protoData) {
            if (mCallback == null) {
                return;
            }

            ParameterizedType pType = (ParameterizedType) mCallback.getClass().getGenericSuperclass();
            if (pType == null) {
                Logger.e(TAG, "pType == null");
                callUnknownError();
                return;
            }

            try {
                Class type = (Class) pType.getActualTypeArguments()[0];
                if (type == Void.class) {
                    mCallback.onCompleted(null);
                    return;
                }
                Method method = type.getMethod("parseFrom", byte[].class);
                T resultMessage = (T) method.invoke(null, (Object) protoData);
                if (resultMessage == null) {
                    Logger.e(TAG, "resultMessage == null");
                    callUnknownError();
                } else {
                    mCallback.onCompleted(resultMessage);
                }

            } catch (Exception e) {
                e.printStackTrace();
                callUnknownError();
            }
        }

        private void callUnknownError() {
            onFailure(ResponseCode.INTERNAL_UNKNOWN.getCode(), ResponseCode.INTERNAL_UNKNOWN.getMsg());
        }

        @Override
        public void onFailure(int code, String error) {
            if (mCallback == null) {
                return;
            }
            mCallback.onFailure(code, error);
        }
    }

    public interface IMServiceConnector {
        void connectRemoteIMService(ConnectCallback callback);
    }

    public interface ConnectCallback {
        void onCompleted(IRemoteService remoteService);

        void onFailure(Throwable t);
    }
}
