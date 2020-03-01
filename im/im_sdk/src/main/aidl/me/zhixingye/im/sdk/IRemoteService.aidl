// IRemoteService.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.IContactManagerHandle;
import me.zhixingye.im.sdk.IConversationManagerHandle;
import me.zhixingye.im.sdk.IGroupManagerHandle;
import me.zhixingye.im.sdk.IMessageManagerHandle;
import me.zhixingye.im.sdk.IStorageManagerHandle;
import me.zhixingye.im.sdk.IUserManagerHandle;
import me.zhixingye.im.sdk.IResultCallback;

interface IRemoteService {
    IContactManagerHandle getContactManagerHandle();

    IConversationManagerHandle getConversationManagerHandle();

    IGroupManagerHandle getGroupManagerHandle();

    IMessageManagerHandle getMessageManagerHandle();

    IStorageManagerHandle getStorageManagerHandle();

    IUserManagerHandle getUserManagerHandle();

    void registerByTelephone(String telephone, String password, String verificationCode, IResultCallback callback);

    void loginByTelephone(String telephone, String password, String verificationCode, IResultCallback callback);

    void loginByEmail(String email, String password, String verificationCode, IResultCallback callback);

    void resetLoginPasswordByTelephone(String telephone, String verificationCode, String newPassword, IResultCallback callback);

    void resetLoginPassword(String telephone, String oldPassword, String newPassword, IResultCallback callback);
}
