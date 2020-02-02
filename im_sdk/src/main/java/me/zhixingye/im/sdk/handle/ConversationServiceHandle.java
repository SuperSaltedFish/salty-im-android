package me.zhixingye.im.sdk.handle;

import android.os.RemoteException;

import me.zhixingye.im.sdk.IConversationServiceHandle;
import me.zhixingye.im.sdk.IResultCallback;

/**
 * Created by zhixingye on 2020年02月02日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class ConversationServiceHandle extends IConversationServiceHandle.Stub {
    @Override
    public void getAllConversations(IResultCallback callback) throws RemoteException {

    }

    @Override
    public void getConversationDetail(String conversationId, int type, IResultCallback callback) throws RemoteException {

    }

    @Override
    public void removeConversation(String conversationId, int type, IResultCallback callback) throws RemoteException {

    }

    @Override
    public void clearConversationMessage(String conversationId, int type, IResultCallback callback) throws RemoteException {

    }

    @Override
    public void updateConversationTitle(String conversationId, int type, String title, IResultCallback callback) throws RemoteException {

    }

    @Override
    public void updateConversationTop(String conversationId, int type, boolean isTop, IResultCallback callback) throws RemoteException {

    }

    @Override
    public void updateNotificationStatus(String conversationId, int type, int status, IResultCallback callback) throws RemoteException {

    }
}
