package me.zhixingye.im.sdk.proxy;


import com.google.protobuf.GeneratedMessageLite;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import me.zhixingye.im.constant.ResponseCode;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.IResultCallback;
import me.zhixingye.im.sdk.util.CallbackUtil;
import me.zhixingye.im.tool.Logger;

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
}
