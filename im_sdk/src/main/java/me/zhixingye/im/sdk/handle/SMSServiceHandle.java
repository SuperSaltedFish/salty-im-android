package me.zhixingye.im.sdk.handle;

import android.os.RemoteException;

import com.salty.protos.ObtainSMSCodeReq;
import com.salty.protos.ObtainSMSCodeResp;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.constant.ResponseCode;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.IResultCallback;
import me.zhixingye.im.sdk.ISMSServiceHandle;
import me.zhixingye.im.sdk.util.CallbackUtil;
import me.zhixingye.im.service.impl.SMSServiceImpl;
import me.zhixingye.im.tool.Logger;

/**
 * Created by zhixingye on 2020年01月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class SMSServiceHandle extends ISMSServiceHandle.Stub  {

    private SMSServiceImpl mSMSService = IMCore.get().getSMSService();

    @Override
    public void obtainVerificationCodeForTelephoneType(String telephone, int type, final IResultCallback callback) throws RemoteException {
        mSMSService.obtainVerificationCodeForTelephoneType(telephone, ObtainSMSCodeReq.CodeType.forNumber(type), new RequestCallback<ObtainSMSCodeResp>() {
            @Override
            public void onCompleted(ObtainSMSCodeResp response) {
                CallbackUtil.callCompleted(callback, null);
            }

            @Override
            public void onFailure(int code, String error) {
                CallbackUtil.callFailure(callback, code,error);
            }
        });
    }
}
