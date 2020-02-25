package me.zhixingye.im.service.manager;

import com.salty.protos.ClearConversationMessageResp;
import com.salty.protos.Conversation;
import com.salty.protos.GetAllConversationResp;
import com.salty.protos.GetConversationDetailResp;
import com.salty.protos.RemoveConversationResp;
import com.salty.protos.UpdateConversationTitleResp;
import com.salty.protos.UpdateConversationTopResp;
import com.salty.protos.UpdateNotificationStatusResp;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.ConversationService;
import me.zhixingye.im.service.impl.ConversationServiceImpl;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class ConversationManager implements ConversationService {

    private ConversationService mService;

    public ConversationManager() {
        mService = new ConversationServiceImpl();
    }

    @Override
    public void getAllConversations(RequestCallback<GetAllConversationResp> callback) {
        mService.getAllConversations(callback);
    }

    @Override
    public void getConversationDetail(String conversationId, Conversation.ConversationType type, RequestCallback<GetConversationDetailResp> callback) {
        mService.getConversationDetail(conversationId, type, callback);
    }

    @Override
    public void removeConversation(String conversationId, Conversation.ConversationType type, RequestCallback<RemoveConversationResp> callback) {
        mService.removeConversation(conversationId, type, callback);
    }

    @Override
    public void clearConversationMessage(String conversationId, Conversation.ConversationType type, RequestCallback<ClearConversationMessageResp> callback) {
        mService.clearConversationMessage(conversationId, type, callback);
    }

    @Override
    public void updateConversationTitle(String conversationId, Conversation.ConversationType type, String title, RequestCallback<UpdateConversationTitleResp> callback) {
        mService.updateConversationTitle(conversationId, type, title, callback);
    }

    @Override
    public void updateConversationTop(String conversationId, Conversation.ConversationType type, boolean isTop, RequestCallback<UpdateConversationTopResp> callback) {
        mService.updateConversationTop(conversationId, type, isTop, callback);
    }

    @Override
    public void updateNotificationStatus(String conversationId, Conversation.ConversationType type, Conversation.NotificationStatus status, RequestCallback<UpdateNotificationStatusResp> callback) {
        mService.updateNotificationStatus(conversationId, type, status, callback);
    }

    public void destroy() {

    }
}
