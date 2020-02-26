// IRemoteService.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.IContactManagerHandle;
import me.zhixingye.im.sdk.IConversationManagerHandle;
import me.zhixingye.im.sdk.IGroupManagerHandle;
import me.zhixingye.im.sdk.IMessageManagerHandle;
import me.zhixingye.im.sdk.ISMSManagerHandle;
import me.zhixingye.im.sdk.IStorageManagerHandle;
import me.zhixingye.im.sdk.IUserManagerHandle;

interface IRemoteService {
    IContactManagerHandle getContactManagerHandle();

    IConversationManagerHandle getConversationManagerHandle();

    IGroupManagerHandle getGroupManagerHandle();

    IMessageManagerHandle getMessageManagerHandle();

    ISMSManagerHandle getSMSManagerHandle();

    IStorageManagerHandle getStorageManagerHandle();

    IUserManagerHandle getUserManagerHandle();
}
