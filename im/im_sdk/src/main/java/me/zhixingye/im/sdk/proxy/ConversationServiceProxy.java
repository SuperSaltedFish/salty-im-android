package me.zhixingye.im.sdk.proxy;

import com.salty.protos.ClearConversationMessageResp;
import com.salty.protos.Conversation;
import com.salty.protos.GetAllConversationResp;
import com.salty.protos.GetConversationDetailResp;
import com.salty.protos.RemoveConversationResp;
import com.salty.protos.UpdateConversationTitleResp;
import com.salty.protos.UpdateConversationTopResp;
import com.salty.protos.UpdateNotificationStatusResp;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.IConversationServiceHandle;
import me.zhixingye.im.sdk.IRemoteService;
import me.zhixingye.im.sdk.util.CallbackUtil;
import me.zhixingye.im.service.ConversationService;
import me.zhixingye.im.tool.Logger;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class ConversationServiceProxy extends BasicProxy implements ConversationService {

    private static final String TAG = "ContactServiceProxy";

    private IConversationServiceHandle mConversationHandle;

    @Override
    public void onBindHandle(IRemoteService service) {
        try {
            mConversationHandle = service.getConversationServiceHandle();
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            mConversationHandle = null;
        }
    }

    @Override
    public void getAllConversations(RequestCallback<GetAllConversationResp> callback) {
        try {
            mConversationHandle.getAllConversations(new ResultCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            callRemoteFail(callback);
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void getConversationDetail(String conversationId, Conversation.ConversationType type, RequestCallback<GetConversationDetailResp> callback) {
        try {
            mConversationHandle.getConversationDetail(conversationId, type.getNumber(), new ResultCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            callRemoteFail(callback);
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void removeConversation(String conversationId, Conversation.ConversationType type, RequestCallback<RemoveConversationResp> callback) {
        try {
            mConversationHandle.removeConversation(conversationId, type.getNumber(), new ResultCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            callRemoteFail(callback);
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void clearConversationMessage(String conversationId, Conversation.ConversationType type, RequestCallback<ClearConversationMessageResp> callback) {
        try {
            mConversationHandle.clearConversationMessage(conversationId, type.getNumber(), new ResultCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            callRemoteFail(callback);
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void updateConversationTitle(String conversationId, Conversation.ConversationType type, String title, RequestCallback<UpdateConversationTitleResp> callback) {
        try {
            mConversationHandle.updateConversationTitle(conversationId, type.getNumber(), title, new ResultCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            callRemoteFail(callback);
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void updateConversationTop(String conversationId, Conversation.ConversationType type, boolean isTop, RequestCallback<UpdateConversationTopResp> callback) {
        try {
            mConversationHandle.updateConversationTop(conversationId, type.getNumber(), isTop, new ResultCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            callRemoteFail(callback);
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void updateNotificationStatus(String conversationId, Conversation.ConversationType type, Conversation.NotificationStatus status, RequestCallback<UpdateNotificationStatusResp> callback) {
        try {
            mConversationHandle.updateNotificationStatus(conversationId, type.getNumber(), status.getNumber(), new ResultCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            callRemoteFail(callback);
            CallbackUtil.callRemoteError(callback);
        }
    }
}
