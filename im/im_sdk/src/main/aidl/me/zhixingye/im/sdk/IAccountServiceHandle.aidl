// IAccountServiceHandle.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements

import me.zhixingye.im.sdk.IResultCallback;

interface IAccountServiceHandle {
   void registerByTelephone(String telephone, String password, String verificationCode, IResultCallback callback);

    void registerByEmail(String email, String password, String verificationCode, IResultCallback callback);

    void loginByTelephone(String telephone, String password, String verificationCode, IResultCallback callback);

    void loginByEmail(String email, String password, String verificationCode, IResultCallback callback);

    void logout();

    String getCurrentUserId();

    String getCurrentUserToken();
}
