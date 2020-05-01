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
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class ConversationServiceProxy implements ConversationService, RemoteProxy {

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
            mConversationHandle.getAllConversations(new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void getConversationDetail(String conversationId, Conversation.ConversationType type, RequestCallback<GetConversationDetailResp> callback) {
        try {
            mConversationHandle.getConversationDetail(conversationId, type.getNumber(), new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void removeConversation(String conversationId, Conversation.ConversationType type, RequestCallback<RemoveConversationResp> callback) {
        try {
            mConversationHandle.removeConversation(conversationId, type.getNumber(), new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void clearConversationMessage(String conversationId, Conversation.ConversationType type, RequestCallback<ClearConversationMessageResp> callback) {
        try {
            mConversationHandle.clearConversationMessage(conversationId, type.getNumber(), new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void updateConversationTitle(String conversationId, Conversation.ConversationType type, String title, RequestCallback<UpdateConversationTitleResp> callback) {
        try {
            mConversationHandle.updateConversationTitle(conversationId, type.getNumber(), title, new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void updateConversationTop(String conversationId, Conversation.ConversationType type, boolean isTop, RequestCallback<UpdateConversationTopResp> callback) {
        try {
            mConversationHandle.updateConversationTop(conversationId, type.getNumber(), isTop, new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void updateNotificationStatus(String conversationId, Conversation.ConversationType type, Conversation.NotificationStatus status, RequestCallback<UpdateNotificationStatusResp> callback) {
        try {
            mConversationHandle.updateNotificationStatus(conversationId, type.getNumber(), status.getNumber(), new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
            CallbackUtil.callRemoteError(callback);
        }
    }
}
