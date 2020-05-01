// IAccountServiceHandle.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements

import me.zhixingye.im.sdk.IRemoteCallback;

interface IAccountServiceHandle {
   void registerByTelephone(String telephone, String password, String verificationCode, IRemoteCallback callback);

    void registerByEmail(String email, String password, String verificationCode, IRemoteCallback callback);

    void loginByTelephone(String telephone, String password, String verificationCode, IRemoteCallback callback);

    void loginByEmail(String email, String password, String verificationCode, IRemoteCallback callback);

    void logout();

    boolean isLogged();

    String getCurrentUserId();

    String getCurrentUserToken();
}
