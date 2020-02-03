package me.zhixingye.im.sdk.proxy;


import com.google.protobuf.GeneratedMessageLite;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.IResultCallback;
import me.zhixingye.im.sdk.util.CallbackUtil;

/**
 * Created by zhixingye on 2020年02月02日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
class BasicProxy {

    static boolean checkServiceState(Object serverHandle, RequestCallback<?> callback) {
        if (serverHandle == null) {
            CallbackUtil.callRemoteError(callback);
            return false;
        }
        return true;
    }

    protected class ResultCallbackWrapper<T extends GeneratedMessageLite> extends IResultCallback.Stub {

        private RequestCallback<T> mCallback;

        ResultCallbackWrapper(RequestCallback<T> callback) {
            mCallback = callback;
        }

        @Override
        public void onCompleted(byte[] protoData) {
            if (mCallback == null) {
                return;
            }
//            ParameterizedType pType = (ParameterizedType) this.getClass().getGenericSuperclass();
//            if (pType != null) {
//                Class type = (Class) pType.getActualTypeArguments()[0];
//                try {
//                    Method method = type.getMethod("parseFrom", byte[].class);
//                    mCallback.onCompleted(method.invoke(null,protoData));
//                } catch (NoSuchMethodException e) {
//                    e.printStackTrace();
//                }
//            }
        }

        @Override
        public void onFailure(int code, String error) {
            if (mCallback == null) {
                return;
            }
            mCallback.onFailure(code, error);
        }
    }
}
