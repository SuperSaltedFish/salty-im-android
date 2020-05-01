package me.zhixingye.im.sdk.util;

import android.os.RemoteException;

import com.google.protobuf.MessageLite;

import me.zhixingye.im.constant.ResponseCode;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.IRemoteCallback;

/**
 * Created by zhixingye on 2020年02月01日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class CallbackUtil {
    public static void callCompleted(IRemoteCallback callback, MessageLite message) {
        if (callback == null) {
            return;
        }
        try {
            callback.onCompleted(message.toByteArray());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void callFailure(IRemoteCallback callback, int code, String error) {
        if (callback == null) {
            return;
        }
        try {
            callback.onFailure(code, error);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void callRemoteError(RequestCallback callback) {
        if (callback != null) {
            ResponseCode responseCode = ResponseCode.INTERNAL_IPC_EXCEPTION;
            callback.onFailure(responseCode.getCode(), responseCode.getMsg());
        }
    }
}
