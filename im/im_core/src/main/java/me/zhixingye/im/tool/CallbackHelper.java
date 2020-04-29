package me.zhixingye.im.tool;

import me.zhixingye.im.constant.ResponseCode;
import me.zhixingye.im.listener.RequestCallback;

/**
 * Created by zhixingye on 2020年02月26日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class CallbackHelper {

    public static <T> void callCompleted(final T response, final RequestCallback<T> callback) {
        if (callback != null) {
            HandlerHelper.getUIHandler().post(new Runnable() {
                @Override
                public void run() {
                    callback.onCompleted(response);
                }
            });
        }
    }

    public static void callFailure(final int code, final String error, final RequestCallback<?> callback) {
        if (callback != null) {
            HandlerHelper.getUIHandler().post(new Runnable() {
                @Override
                public void run() {
                    callback.onFailure(code, error);
                }
            });
        }
    }

    public static void callFailure(final ResponseCode code, final RequestCallback<?> callback) {
        if (callback != null) {
            HandlerHelper.getUIHandler().post(new Runnable() {
                @Override
                public void run() {
                    callback.onFailure(code.getCode(), code.getMsg());
                }
            });
        }
    }
}
