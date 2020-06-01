// IAccountServiceHandle.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements

import me.zhixingye.im.sdk.IRemoteCallback;

interface IAccountServiceHandle {
   void registerByTelephone(String telephone, String password, IRemoteCallback callback);

    void registerByEmail(String email, String password, IRemoteCallback callback);

    void resetLoginPasswordByEmail(String email, String newPassword, IRemoteCallback callback);

    void resetLoginPasswordByTelephone(String telephone, String newPassword, IRemoteCallback callback);

    void loginByTelephone(String telephone, String password, IRemoteCallback callback);

    void loginByEmail(String email, String password, IRemoteCallback callback);

    void loginByLastLoginInfo(IRemoteCallback callback);

    void logout();

    boolean isLogged();

    String getCurrentUserId();

    String getCurrentUserToken();

    byte[] getCurrentUserProfile();
}
