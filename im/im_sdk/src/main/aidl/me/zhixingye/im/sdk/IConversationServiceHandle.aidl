// IConversationServiceHandle.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.IRemoteCallback;

interface IConversationServiceHandle {
    void getAllConversations(IRemoteCallback callback);

    void getConversationDetail(String conversationId, int type, IRemoteCallback callback);

    void removeConversation(String conversationId, int type, IRemoteCallback callback);

    void clearConversationMessage(String conversationId, int type, IRemoteCallback callback);

    void updateConversationTitle(String conversationId, int type, String title, IRemoteCallback callback);

    void updateConversationTop(String conversationId, int type, boolean isTop, IRemoteCallback callback);

    void updateNotificationStatus(String conversationId, int type, int status, IRemoteCallback callback);
}
