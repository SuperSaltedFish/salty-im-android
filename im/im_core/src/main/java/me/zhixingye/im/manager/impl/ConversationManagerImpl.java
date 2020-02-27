package me.zhixingye.im.manager.impl;

import com.salty.protos.ClearConversationMessageResp;
import com.salty.protos.Conversation;
import com.salty.protos.GetAllConversationResp;
import com.salty.protos.GetConversationDetailResp;
import com.salty.protos.RemoveConversationResp;
import com.salty.protos.UpdateConversationTitleResp;
import com.salty.protos.UpdateConversationTopResp;
import com.salty.protos.UpdateNotificationStatusResp;

import me.zhixingye.im.api.ConversationApi;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.manager.ConversationManager;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class ConversationManagerImpl implements ConversationManager {

    private ConversationApi mConversationApi;

    public ConversationManagerImpl(String userId, ConversationApi conversationApi) {
        mConversationApi = conversationApi;
    }

    @Override
    public void getAllConversations(RequestCallback<GetAllConversationResp> callback) {
        mConversationApi.getAllConversations(callback);
    }

    @Override
    public void getConversationDetail(String conversationId, Conversation.ConversationType type, RequestCallback<GetConversationDetailResp> callback) {
        mConversationApi.getConversationDetail(conversationId, type, callback);
    }

    @Override
    public void removeConversation(String conversationId, Conversation.ConversationType type, RequestCallback<RemoveConversationResp> callback) {
        mConversationApi.removeConversation(conversationId, type, callback);
    }

    @Override
    public void clearConversationMessage(String conversationId, Conversation.ConversationType type, RequestCallback<ClearConversationMessageResp> callback) {
        mConversationApi.clearConversationMessage(conversationId, type, callback);
    }

    @Override
    public void updateConversationTitle(String conversationId, Conversation.ConversationType type, String title, RequestCallback<UpdateConversationTitleResp> callback) {
        mConversationApi.updateConversationTitle(conversationId, type, title, callback);
    }

    @Override
    public void updateConversationTop(String conversationId, Conversation.ConversationType type, boolean isTop, RequestCallback<UpdateConversationTopResp> callback) {
        mConversationApi.updateConversationTop(conversationId, type, isTop, callback);
    }

    @Override
    public void updateNotificationStatus(String conversationId, Conversation.ConversationType type, Conversation.NotificationStatus status, RequestCallback<UpdateNotificationStatusResp> callback) {
        mConversationApi.updateNotificationStatus(conversationId, type, status, callback);
    }

}
