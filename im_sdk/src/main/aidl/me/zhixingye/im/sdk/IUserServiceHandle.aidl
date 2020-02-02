// IUserServiceHandle.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.IResultCallback;

interface IUserServiceHandle {
    void registerByTelephone(String telephone, String password, String verificationCode, IResultCallback callback);

    void loginByTelephone(String telephone, String password, String verificationCode, IResultCallback callback);
}
