package me.zhixingye.im.sdk.handle;

import android.os.RemoteException;

import com.google.protobuf.MessageLite;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.IRemoteCallback;

/**
 * Created by zhixingye on 2020年05月01日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class ByteRemoteCallback<T extends MessageLite> implements RequestCallback<T> {

    private IRemoteCallback mRemoteCallback;

    public ByteRemoteCallback(IRemoteCallback remoteCallback) {
        mRemoteCallback = remoteCallback;
    }

    @Override
    public void onCompleted(T response) {
        if (mRemoteCallback != null) {
            try {
                mRemoteCallback.onCompleted(response.toByteArray());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(int code, String error) {
        if (mRemoteCallback != null) {
            try {
                mRemoteCallback.onFailure(code, error);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
