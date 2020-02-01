package me.zhixingye.im.sdk.util;

import android.os.Parcelable;
import android.os.RemoteException;

import me.zhixingye.im.constant.ErrorCode;
import me.zhixingye.im.sdk.IResultCallback;
import me.zhixingye.im.sdk.RemoteResultWrap;

/**
 * Created by zhixingye on 2020年02月01日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class CallbackUtil {
    public static void callCompleted(IResultCallback callback, Parcelable parcelable) {
        if (callback == null) {
            return;
        }
        try {
            callback.onComplete(new RemoteResultWrap(parcelable));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void callFailure(IResultCallback callback, ErrorCode code) {
        if (callback == null) {
            return;
        }
        try {
            callback.onFailure(code.getCode(), code.getMsg());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
