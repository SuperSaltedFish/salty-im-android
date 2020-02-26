package me.zhixingye.im.sdk.handle;

import com.salty.protos.ObtainSMSCodeReq;
import com.salty.protos.ObtainSMSCodeResp;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.manager.SMSManager;
import me.zhixingye.im.sdk.IResultCallback;
import me.zhixingye.im.sdk.ISMSManagerHandle;
import me.zhixingye.im.sdk.util.CallbackUtil;

/**
 * Created by zhixingye on 2020年01月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class SMSManagerHandle extends ISMSManagerHandle.Stub {

    private SMSManager mSMSService = IMCore.get().getSMSManager();

    @Override
    public void obtainVerificationCodeForTelephoneType(String telephone, int type, final IResultCallback callback) {
        mSMSService.obtainVerificationCodeForTelephoneType(telephone, ObtainSMSCodeReq.CodeType.forNumber(type), new RequestCallback<ObtainSMSCodeResp>() {
            @Override
            public void onCompleted(ObtainSMSCodeResp response) {
                CallbackUtil.callCompleted(callback, response);
            }

            @Override
            public void onFailure(int code, String error) {
                CallbackUtil.callFailure(callback, code, error);
            }
        });
    }
}
