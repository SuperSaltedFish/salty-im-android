package me.zhixingye.im.sdk.proxy;


import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.IResultCallback;
import me.zhixingye.im.sdk.RemoteResultWrap;

/**
 * Created by zhixingye on 2020年02月02日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
class BasicProxy {

    protected class ResultCallbackWrapper<T> extends IResultCallback.Stub {

        private RequestCallback<T> mCallback;

        ResultCallbackWrapper(RequestCallback<T> callback) {
            mCallback = callback;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onCompleted(RemoteResultWrap result) {
            if (mCallback == null) {
                return;
            }
            mCallback.onCompleted((T) result.getResult());
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
