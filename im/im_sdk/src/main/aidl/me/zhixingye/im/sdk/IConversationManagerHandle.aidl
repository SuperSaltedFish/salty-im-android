// IConversationManagerHandle.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.IResultCallback;

interface IConversationManagerHandle {
    void getAllConversations(IResultCallback callback);

    void getConversationDetail(String conversationId, int type, IResultCallback callback);

    void removeConversation(String conversationId, int type, IResultCallback callback);

    void clearConversationMessage(String conversationId, int type, IResultCallback callback);

    void updateConversationTitle(String conversationId, int type, String title, IResultCallback callback);

    void updateConversationTop(String conversationId, int type, boolean isTop, IResultCallback callback);

    void updateNotificationStatus(String conversationId, int type, int status, IResultCallback callback);
}
