package me.zhixingye.im.sdk.proxy;

import android.os.RemoteException;

import com.salty.protos.ClearConversationMessageResp;
import com.salty.protos.Conversation;
import com.salty.protos.GetAllConversationResp;
import com.salty.protos.GetConversationDetailResp;
import com.salty.protos.RemoveConversationResp;
import com.salty.protos.UpdateConversationTitleResp;
import com.salty.protos.UpdateConversationTopResp;
import com.salty.protos.UpdateNotificationStatusResp;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.IConversationManagerHandle;
import me.zhixingye.im.sdk.util.CallbackUtil;
import me.zhixingye.im.manager.ConversationManager;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class ConversationManagerProxy extends BasicProxy implements ConversationManager {

    private IConversationManagerHandle mManagerHandle;

    public ConversationManagerProxy() {
    }

    public void bindHandle(IConversationManagerHandle handle) {
        mManagerHandle = handle;
    }

    public void unbindHandle() {
        mManagerHandle = null;
    }

    @Override
    public void getAllConversations(RequestCallback<GetAllConversationResp> callback) {
        if (isServiceUnavailable(mManagerHandle, callback)) {
            return;
        }
        try {
            mManagerHandle.getAllConversations(new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void getConversationDetail(String conversationId, Conversation.ConversationType type, RequestCallback<GetConversationDetailResp> callback) {
        if (isServiceUnavailable(mManagerHandle, callback)) {
            return;
        }
        try {
            mManagerHandle.getConversationDetail(conversationId, type.getNumber(), new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void removeConversation(String conversationId, Conversation.ConversationType type, RequestCallback<RemoveConversationResp> callback) {
        if (isServiceUnavailable(mManagerHandle, callback)) {
            return;
        }
        try {
            mManagerHandle.removeConversation(conversationId, type.getNumber(), new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void clearConversationMessage(String conversationId, Conversation.ConversationType type, RequestCallback<ClearConversationMessageResp> callback) {
        if (isServiceUnavailable(mManagerHandle, callback)) {
            return;
        }
        try {
            mManagerHandle.clearConversationMessage(conversationId, type.getNumber(), new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void updateConversationTitle(String conversationId, Conversation.ConversationType type, String title, RequestCallback<UpdateConversationTitleResp> callback) {
        if (isServiceUnavailable(mManagerHandle, callback)) {
            return;
        }
        try {
            mManagerHandle.updateConversationTitle(conversationId, type.getNumber(), title, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void updateConversationTop(String conversationId, Conversation.ConversationType type, boolean isTop, RequestCallback<UpdateConversationTopResp> callback) {
        if (isServiceUnavailable(mManagerHandle, callback)) {
            return;
        }
        try {
            mManagerHandle.updateConversationTop(conversationId, type.getNumber(), isTop, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void updateNotificationStatus(String conversationId, Conversation.ConversationType type, Conversation.NotificationStatus status, RequestCallback<UpdateNotificationStatusResp> callback) {
        if (isServiceUnavailable(mManagerHandle, callback)) {
            return;
        }
        try {
            mManagerHandle.updateNotificationStatus(conversationId, type.getNumber(), status.getNumber(), new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }
}
