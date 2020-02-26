package me.zhixingye.im.sdk.proxy;

import android.os.RemoteException;

import com.salty.protos.ObtainSMSCodeReq;
import com.salty.protos.ObtainSMSCodeResp;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.ISMSServiceHandle;
import me.zhixingye.im.sdk.util.CallbackUtil;
import me.zhixingye.im.manager.SMSManager;

/**
 * Created by zhixingye on 2020年01月10日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class SMSManagerProxy extends BasicProxy implements SMSManager {

    private ISMSServiceHandle mServiceHandle;

    public SMSManagerProxy() {
    }

    public void bindHandle(ISMSServiceHandle handle) {
        mServiceHandle = handle;
    }

    public void unbindHandle() {
        mServiceHandle = null;
    }

    @Override
    public void obtainVerificationCodeForTelephoneType(String telephone, ObtainSMSCodeReq.CodeType type, RequestCallback<ObtainSMSCodeResp> callback) {
        if (isServiceUnavailable(mServiceHandle, callback)) {
            return;
        }
        try {
            mServiceHandle.obtainVerificationCodeForTelephoneType(telephone, type.getNumber(), new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }
}
