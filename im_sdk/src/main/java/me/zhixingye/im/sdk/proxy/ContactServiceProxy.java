package me.zhixingye.im.sdk.proxy;

import android.os.RemoteException;

import com.salty.protos.AcceptContactResp;
import com.salty.protos.DeleteContactResp;
import com.salty.protos.RefusedContactResp;
import com.salty.protos.RequestContactResp;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.IContactServiceHandle;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class ContactServiceProxy extends BasicProxy implements me.zhixingye.im.service.ContactService {

    private IContactServiceHandle mServiceHandle;

    public ContactServiceProxy() {
    }

    public void bindHandle(IContactServiceHandle handle) {
        mServiceHandle = handle;
    }

    public void unbindHandle() {
        mServiceHandle = null;
    }

    @Override
    public void requestContact(String userId, String reason, RequestCallback<RequestContactResp> callback) {
        try {
            mServiceHandle.requestContact(userId, reason, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refusedContact(String userId, String reason, RequestCallback<RefusedContactResp> callback) {

    }

    @Override
    public void acceptContact(String userId, RequestCallback<AcceptContactResp> callback) {

    }

    @Override
    public void deleteContact(String userId, RequestCallback<DeleteContactResp> callback) {

    }
}
