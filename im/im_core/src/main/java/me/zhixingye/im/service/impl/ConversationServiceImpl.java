package me.zhixingye.im.service.impl;

import com.salty.protos.ClearConversationMessageResp;
import com.salty.protos.Conversation;
import com.salty.protos.GetAllConversationResp;
import com.salty.protos.GetConversationDetailResp;
import com.salty.protos.RemoveConversationResp;
import com.salty.protos.UpdateConversationTitleResp;
import com.salty.protos.UpdateConversationTopResp;
import com.salty.protos.UpdateNotificationStatusResp;

import me.zhixingye.im.api.ContactApi;
import me.zhixingye.im.api.ConversationApi;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.ApiService;
import me.zhixingye.im.service.ConversationService;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class ConversationServiceImpl implements ConversationService {


    public ConversationServiceImpl() {
    }

    @Override
    public void getAllConversations(RequestCallback<GetAllConversationResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(ConversationApi.class)
                .getAllConversations(callback);
    }

    @Override
    public void getConversationDetail(String conversationId, Conversation.ConversationType type, RequestCallback<GetConversationDetailResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(ConversationApi.class)
                .getConversationDetail(conversationId, type, callback);
    }

    @Override
    public void removeConversation(String conversationId, Conversation.ConversationType type, RequestCallback<RemoveConversationResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(ConversationApi.class)
                .removeConversation(conversationId, type, callback);
    }

    @Override
    public void clearConversationMessage(String conversationId, Conversation.ConversationType type, RequestCallback<ClearConversationMessageResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(ConversationApi.class)
                .clearConversationMessage(conversationId, type, callback);
    }

    @Override
    public void updateConversationTitle(String conversationId, Conversation.ConversationType type, String title, RequestCallback<UpdateConversationTitleResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(ConversationApi.class)
                .updateConversationTitle(conversationId, type, title, callback);
    }

    @Override
    public void updateConversationTop(String conversationId, Conversation.ConversationType type, boolean isTop, RequestCallback<UpdateConversationTopResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(ConversationApi.class)
                .updateConversationTop(conversationId, type, isTop, callback);
    }

    @Override
    public void updateNotificationStatus(String conversationId, Conversation.ConversationType type, Conversation.NotificationStatus status, RequestCallback<UpdateNotificationStatusResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(ConversationApi.class)
                .updateNotificationStatus(conversationId, type, status, callback);
    }

}
