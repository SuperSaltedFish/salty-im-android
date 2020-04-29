// IUserServiceHandle.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.IResultCallback;

interface IUserServiceHandle {
    void updateUserInfo(String nickname, String description, int sex, long birthday, String location, IResultCallback callback);

    void getUserInfoByUserId(String userId, IResultCallback callback);

    void queryUserInfoByTelephone(String telephone, IResultCallback callback);

    void queryUserInfoByEmail(String email, IResultCallback callback);
}
