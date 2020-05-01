package me.zhixingye.im.sdk.handle;

import com.salty.protos.ClearConversationMessageResp;
import com.salty.protos.Conversation;
import com.salty.protos.GetAllConversationResp;
import com.salty.protos.GetConversationDetailResp;
import com.salty.protos.RemoveConversationResp;
import com.salty.protos.UpdateConversationTitleResp;
import com.salty.protos.UpdateConversationTopResp;
import com.salty.protos.UpdateNotificationStatusResp;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.sdk.IConversationServiceHandle;
import me.zhixingye.im.sdk.IRemoteCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class ConversationServiceHandle extends IConversationServiceHandle.Stub {
    @Override
    public void getAllConversations(IRemoteCallback callback) {
        IMCore.get().getConversationService()
                .getAllConversations(
                        new ByteRemoteCallback<GetAllConversationResp>(callback));
    }

    @Override
    public void getConversationDetail(String conversationId, int type, IRemoteCallback callback) {
        IMCore.get().getConversationService()
                .getConversationDetail(
                        conversationId,
                        Conversation.ConversationType.forNumber(type),
                        new ByteRemoteCallback<GetConversationDetailResp>(callback));
    }

    @Override
    public void removeConversation(String conversationId, int type, IRemoteCallback callback) {
        IMCore.get().getConversationService()
                .removeConversation(
                        conversationId,
                        Conversation.ConversationType.forNumber(type),
                        new ByteRemoteCallback<RemoveConversationResp>(callback));
    }

    @Override
    public void clearConversationMessage(String conversationId, int type, IRemoteCallback callback) {
        IMCore.get().getConversationService()
                .clearConversationMessage(
                        conversationId,
                        Conversation.ConversationType.forNumber(type),
                        new ByteRemoteCallback<ClearConversationMessageResp>(callback));
    }

    @Override
    public void updateConversationTitle(String conversationId, int type, String title, IRemoteCallback callback) {
        IMCore.get().getConversationService()
                .updateConversationTitle(
                        conversationId,
                        Conversation.ConversationType.forNumber(type),
                        title,
                        new ByteRemoteCallback<UpdateConversationTitleResp>(callback));
    }

    @Override
    public void updateConversationTop(String conversationId, int type, boolean isTop, IRemoteCallback callback) {
        IMCore.get().getConversationService()
                .updateConversationTop(
                        conversationId,
                        Conversation.ConversationType.forNumber(type),
                        isTop,
                        new ByteRemoteCallback<UpdateConversationTopResp>(callback));
    }

    @Override
    public void updateNotificationStatus(String conversationId, int type, int status, IRemoteCallback callback) {
        IMCore.get().getConversationService()
                .updateNotificationStatus(
                        conversationId,
                        Conversation.ConversationType.forNumber(type),
                        Conversation.NotificationStatus.forNumber(status),
                        new ByteRemoteCallback<UpdateNotificationStatusResp>(callback));
    }
}
