// IUserServiceHandle.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.IResultCallback;

interface IUserServiceHandle {
    void registerByTelephone(String telephone, String password, String verificationCode, IResultCallback callback);

    void loginByTelephone(String telephone, String password, String verificationCode, IResultCallback callback);

    void loginByEmail(String email, String password, String verificationCode, IResultCallback callback);

    void resetLoginPasswordByTelephone(String telephone, String verificationCode, String newPassword, IResultCallback callback);

    void resetLoginPassword(String telephone, String oldPassword, String newPassword, IResultCallback callback);

    void updateUserInfo(String nickname, String description, int sex, long birthday, String location, IResultCallback callback);

    void getUserInfoByUserId(String userId, IResultCallback callback);

    void queryUserInfoByTelephone(String telephone, IResultCallback callback);

    void queryUserInfoByEmail(String email, IResultCallback callback);
}
