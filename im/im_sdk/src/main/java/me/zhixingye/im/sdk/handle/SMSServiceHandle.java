package me.zhixingye.im.sdk.handle;

import com.salty.protos.ObtainSMSCodeReq;
import com.salty.protos.ObtainSMSCodeResp;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.sdk.IRemoteCallback;
import me.zhixingye.im.sdk.ISMSServiceHandle;

/**
 * Created by zhixingye on 2020年04月29日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class SMSServiceHandle extends ISMSServiceHandle.Stub {
    @Override
    public void obtainVerificationCodeForTelephoneType(String telephone, int codeType, IRemoteCallback callback) {
        IMCore.get().getSMSService()
                .obtainVerificationCodeForTelephoneType(
                        telephone,
                        ObtainSMSCodeReq.CodeType.forNumber(codeType),
                        new ByteRemoteCallback<ObtainSMSCodeResp>(callback));
    }
}
