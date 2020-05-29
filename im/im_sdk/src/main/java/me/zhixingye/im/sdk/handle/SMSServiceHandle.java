package me.zhixingye.im.sdk.handle;


import android.os.RemoteException;

import com.salty.protos.ObtainTelephoneSMSCodeReq;
import com.salty.protos.ObtainTelephoneSMSCodeResp;
import com.salty.protos.SMSOperationType;
import com.salty.protos.VerifyTelephoneSMSCodeResp;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.sdk.IRemoteCallback;
import me.zhixingye.im.sdk.ISMSServiceHandle;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class SMSServiceHandle extends ISMSServiceHandle.Stub {
    @Override
    public void obtainVerificationCodeForTelephoneType(String telephone, int codeType, IRemoteCallback callback) {
        IMCore.get().getSMSService()
                .obtainVerificationCodeForTelephoneType(
                        telephone,
                        SMSOperationType.forNumber(codeType),
                        new ByteRemoteCallback<ObtainTelephoneSMSCodeResp>(callback));
    }

    @Override
    public void verifyTelephoneSMSCode(String telephone, String smsCode, int codeType, IRemoteCallback callback) {
        IMCore.get().getSMSService()
                .verifyTelephoneSMSCode(
                        telephone,
                        smsCode,
                        SMSOperationType.forNumber(codeType),
                        new ByteRemoteCallback<VerifyTelephoneSMSCodeResp>(callback));
    }
}
