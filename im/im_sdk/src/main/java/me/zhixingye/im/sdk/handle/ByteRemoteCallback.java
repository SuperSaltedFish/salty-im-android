package me.zhixingye.im.sdk.handle;

import android.os.RemoteException;

import com.google.protobuf.MessageLite;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.IRemoteCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
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
