package me.zhixingye.im.sdk.proxy;

import android.os.RemoteException;

import com.salty.protos.ObtainSMSCodeReq;
import com.salty.protos.ObtainSMSCodeResp;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.manager.SMSManager;
import me.zhixingye.im.sdk.ISMSManagerHandle;
import me.zhixingye.im.sdk.util.CallbackUtil;

/**
 * Created by zhixingye on 2020年01月10日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class SMSManagerProxy extends BasicProxy implements SMSManager {

    private ISMSManagerHandle mManagerHandle;

    public SMSManagerProxy() {
    }

    public void bindHandle(ISMSManagerHandle handle) {
        mManagerHandle = handle;
    }

    public void unbindHandle() {
        mManagerHandle = null;
    }

    @Override
    public void obtainVerificationCodeForTelephoneType(String telephone, ObtainSMSCodeReq.CodeType type, RequestCallback<ObtainSMSCodeResp> callback) {
        if (isServiceUnavailable(mManagerHandle, callback)) {
            return;
        }
        try {
            mManagerHandle.obtainVerificationCodeForTelephoneType(telephone, type.getNumber(), new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }
}
