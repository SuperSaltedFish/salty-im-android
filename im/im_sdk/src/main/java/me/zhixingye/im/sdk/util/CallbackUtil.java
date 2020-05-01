package me.zhixingye.im.sdk.util;

import android.os.RemoteException;

import com.google.protobuf.MessageLite;

import me.zhixingye.im.constant.ResponseCode;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.IRemoteCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
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
