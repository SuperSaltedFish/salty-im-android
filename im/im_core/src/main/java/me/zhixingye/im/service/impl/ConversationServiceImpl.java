package me.zhixingye.im.service.impl;

import com.salty.protos.ClearConversationMessageReq;
import com.salty.protos.ClearConversationMessageResp;
import com.salty.protos.Conversation;
import com.salty.protos.ConversationServiceGrpc;
import com.salty.protos.GetAllConversationReq;
import com.salty.protos.GetAllConversationResp;
import com.salty.protos.GetConversationDetailReq;
import com.salty.protos.GetConversationDetailResp;
import com.salty.protos.RemoveConversationReq;
import com.salty.protos.RemoveConversationResp;
import com.salty.protos.UpdateConversationTitleReq;
import com.salty.protos.UpdateConversationTitleResp;
import com.salty.protos.UpdateConversationTopReq;
import com.salty.protos.UpdateConversationTopResp;
import com.salty.protos.UpdateNotificationStatusReq;
import com.salty.protos.UpdateNotificationStatusResp;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.ConversationService;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class ConversationServiceImpl extends BasicService implements ConversationService {

    private ConversationServiceGrpc.ConversationServiceStub mConversationServiceStub;

    public ConversationServiceImpl() {
        mConversationServiceStub = ConversationServiceGrpc.newStub(getChannel());
    }

    @Override
    public void getAllConversations(RequestCallback<GetAllConversationResp> callback) {
        GetAllConversationReq req = GetAllConversationReq.newBuilder()
                .build();
        mConversationServiceStub.getAllConversation(createReq(req), new DefaultStreamObserver<>(callback));
    }

    @Override
    public void getConversationDetail(String conversationId, Conversation.ConversationType type, RequestCallback<GetConversationDetailResp> callback) {
        GetConversationDetailReq req = GetConversationDetailReq.newBuilder()
                .setConversationId(conversationId)
                .setConversationType(type)
                .build();
        mConversationServiceStub.getConversationDetail(createReq(req), new DefaultStreamObserver<>(callback));
    }

    @Override
    public void removeConversation(String conversationId, Conversation.ConversationType type, RequestCallback<RemoveConversationResp> callback) {
        RemoveConversationReq req = RemoveConversationReq.newBuilder()
                .setConversationId(conversationId)
                .setConversationType(type)
                .build();
        mConversationServiceStub.removeConversation(createReq(req), new DefaultStreamObserver<>(callback));
    }

    @Override
    public void clearConversationMessage(String conversationId, Conversation.ConversationType type, RequestCallback<ClearConversationMessageResp> callback) {
        ClearConversationMessageReq req = ClearConversationMessageReq.newBuilder()
                .setConversationId(conversationId)
                .setConversationType(type)
                .build();
        mConversationServiceStub.clearConversationMessage(createReq(req), new DefaultStreamObserver<>(callback));
    }

    @Override
    public void updateConversationTitle(String conversationId, Conversation.ConversationType type, String title, RequestCallback<UpdateConversationTitleResp> callback) {
        UpdateConversationTitleReq req = UpdateConversationTitleReq.newBuilder()
                .setConversationId(conversationId)
                .setConversationType(type)
                .setTitle(title)
                .build();
        mConversationServiceStub.getConversationDetail(createReq(req), new DefaultStreamObserver<>(callback));
    }

    @Override
    public void updateConversationTop(String conversationId, Conversation.ConversationType type, boolean isTop, RequestCallback<UpdateConversationTopResp> callback) {
        UpdateConversationTopReq req = UpdateConversationTopReq.newBuilder()
                .setConversationId(conversationId)
                .setConversationType(type)
                .setIsTop(isTop)
                .build();
        mConversationServiceStub.getConversationDetail(createReq(req), new DefaultStreamObserver<>(callback));
    }

    @Override
    public void updateNotificationStatus(String conversationId, Conversation.ConversationType type, Conversation.NotificationStatus status, RequestCallback<UpdateNotificationStatusResp> callback) {
        UpdateNotificationStatusReq req = UpdateNotificationStatusReq.newBuilder()
                .setConversationId(conversationId)
                .setConversationType(type)
                .setNotificationStatus(status)
                .build();
        mConversationServiceStub.getConversationDetail(createReq(req), new DefaultStreamObserver<>(callback));
    }

    public void destroy() {

    }
}
