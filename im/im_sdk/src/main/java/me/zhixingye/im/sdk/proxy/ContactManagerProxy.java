package me.zhixingye.im.sdk.proxy;

import android.os.RemoteException;

import com.salty.protos.AcceptContactResp;
import com.salty.protos.DeleteContactResp;
import com.salty.protos.RefusedContactResp;
import com.salty.protos.RequestContactResp;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.IContactServiceHandle;
import me.zhixingye.im.sdk.util.CallbackUtil;
import me.zhixingye.im.manager.ContactManager;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class ContactManagerProxy extends BasicProxy implements ContactManager {

    private IContactServiceHandle mServiceHandle;

    public ContactManagerProxy() {
    }

    public void bindHandle(IContactServiceHandle handle) {
        mServiceHandle = handle;
    }

    public void unbindHandle() {
        mServiceHandle = null;
    }

    @Override
    public void requestContact(String userId, String reason, RequestCallback<RequestContactResp> callback) {
        if (isServiceUnavailable(mServiceHandle, callback)) {
            return;
        }
        try {
            mServiceHandle.requestContact(userId, reason, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void refusedContact(String userId, String reason, RequestCallback<RefusedContactResp> callback) {
        if (isServiceUnavailable(mServiceHandle, callback)) {
            return;
        }
        try {
            mServiceHandle.refusedContact(userId, reason, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void acceptContact(String userId, RequestCallback<AcceptContactResp> callback) {
        if (isServiceUnavailable(mServiceHandle, callback)) {
            return;
        }
        try {
            mServiceHandle.acceptContact(userId, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void deleteContact(String userId, RequestCallback<DeleteContactResp> callback) {
        if (isServiceUnavailable(mServiceHandle, callback)) {
            return;
        }
        try {
            mServiceHandle.deleteContact(userId, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }
}
