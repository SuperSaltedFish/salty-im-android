// IRemoteService.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.IContactServiceHandle;
import me.zhixingye.im.sdk.IConversationServiceHandle;
import me.zhixingye.im.sdk.IGroupServiceHandle;
import me.zhixingye.im.sdk.IMessageServiceHandle;
import me.zhixingye.im.sdk.ISMSServiceHandle;
import me.zhixingye.im.sdk.IStorageServiceHandle;
import me.zhixingye.im.sdk.IUserServiceHandle;

interface IRemoteService {
    IContactServiceHandle getContactServiceHandle();

    IConversationServiceHandle getConversationServiceHandle();

    IGroupServiceHandle getGroupServiceHandle();

    IMessageServiceHandle getMessageServiceHandle();

    ISMSServiceHandle getSMSServiceHandle();

    IStorageServiceHandle getStorageServiceHandle();

    IUserServiceHandle getUserServiceHandle();
}
