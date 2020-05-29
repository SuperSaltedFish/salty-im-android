// ISMSServiceHandle.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.IRemoteCallback;

interface ISMSServiceHandle {
    void obtainVerificationCodeForTelephoneType(String telephone, int codeType, IRemoteCallback callback);

    void verifyTelephoneSMSCode(String telephone, String smsCode, int codeType, IRemoteCallback callback);
}