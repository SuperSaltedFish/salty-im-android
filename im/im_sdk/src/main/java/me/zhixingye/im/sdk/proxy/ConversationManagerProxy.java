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
import me.zhixingye.im.sdk.IConversationServiceHandle;
import me.zhixingye.im.sdk.util.CallbackUtil;
import me.zhixingye.im.manager.ConversationManager;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class ConversationManagerProxy extends BasicProxy implements ConversationManager {

    private IConversationServiceHandle mServiceHandle;

    public ConversationManagerProxy() {
    }

    public void bindHandle(IConversationServiceHandle handle) {
        mServiceHandle = handle;
    }

    public void unbindHandle() {
        mServiceHandle = null;
    }

    @Override
    public void getAllConversations(RequestCallback<GetAllConversationResp> callback) {
        if (isServiceUnavailable(mServiceHandle, callback)) {
            return;
        }
        try {
            mServiceHandle.getAllConversations(new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void getConversationDetail(String conversationId, Conversation.ConversationType type, RequestCallback<GetConversationDetailResp> callback) {
        if (isServiceUnavailable(mServiceHandle, callback)) {
            return;
        }
        try {
            mServiceHandle.getConversationDetail(conversationId, type.getNumber(), new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void removeConversation(String conversationId, Conversation.ConversationType type, RequestCallback<RemoveConversationResp> callback) {
        if (isServiceUnavailable(mServiceHandle, callback)) {
            return;
        }
        try {
            mServiceHandle.removeConversation(conversationId, type.getNumber(), new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void clearConversationMessage(String conversationId, Conversation.ConversationType type, RequestCallback<ClearConversationMessageResp> callback) {
        if (isServiceUnavailable(mServiceHandle, callback)) {
            return;
        }
        try {
            mServiceHandle.clearConversationMessage(conversationId, type.getNumber(), new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void updateConversationTitle(String conversationId, Conversation.ConversationType type, String title, RequestCallback<UpdateConversationTitleResp> callback) {
        if (isServiceUnavailable(mServiceHandle, callback)) {
            return;
        }
        try {
            mServiceHandle.updateConversationTitle(conversationId, type.getNumber(), title, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void updateConversationTop(String conversationId, Conversation.ConversationType type, boolean isTop, RequestCallback<UpdateConversationTopResp> callback) {
        if (isServiceUnavailable(mServiceHandle, callback)) {
            return;
        }
        try {
            mServiceHandle.updateConversationTop(conversationId, type.getNumber(), isTop, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void updateNotificationStatus(String conversationId, Conversation.ConversationType type, Conversation.NotificationStatus status, RequestCallback<UpdateNotificationStatusResp> callback) {
        if (isServiceUnavailable(mServiceHandle, callback)) {
            return;
        }
        try {
            mServiceHandle.updateNotificationStatus(conversationId, type.getNumber(), status.getNumber(), new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }
}
