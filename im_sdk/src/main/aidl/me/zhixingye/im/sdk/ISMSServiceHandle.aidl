// ISMSServiceHandle.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.IResultCallback;

interface ISMSServiceHandle {
  void obtainVerificationCodeForTelephoneType(String telephone, int type, IResultCallback callback) ;

}
