package me.zhixingye.im.sdk.proxy;

import android.os.RemoteException;

import com.salty.protos.AcceptContactResp;
import com.salty.protos.DeleteContactResp;
import com.salty.protos.RefusedContactResp;
import com.salty.protos.RequestContactResp;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.IContactServiceHandle;
import me.zhixingye.im.sdk.IRemoteService;
import me.zhixingye.im.sdk.util.CallbackUtil;
import me.zhixingye.im.service.ContactService;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class ContactServiceProxy extends BasicProxy implements ContactService {

    private IContactServiceHandle mContactHandle;

    public ContactServiceProxy(IMServiceConnector proxy) {
        super(proxy);
    }

    @Override
    protected void onConnectRemoteService(IRemoteService service) {
        try {
            mContactHandle = service.getContactServiceHandle();
        } catch (RemoteException e) {
            e.printStackTrace();
            mContactHandle = null;
        }
    }


    @Override
    public void requestContact(String userId, String reason, RequestCallback<RequestContactResp> callback) {
        try {
            mContactHandle.requestContact(userId, reason, new ResultCallbackWrapper<>(callback));
        } catch (Exception e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void refusedContact(String userId, String reason, RequestCallback<RefusedContactResp> callback) {
        try {
            mContactHandle.refusedContact(userId, reason, new ResultCallbackWrapper<>(callback));
        } catch (Exception e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void acceptContact(String userId, RequestCallback<AcceptContactResp> callback) {
        try {
            mContactHandle.acceptContact(userId, new ResultCallbackWrapper<>(callback));
        } catch (Exception e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void deleteContact(String userId, RequestCallback<DeleteContactResp> callback) {
        try {
            mContactHandle.deleteContact(userId, new ResultCallbackWrapper<>(callback));
        } catch (Exception e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }
}
