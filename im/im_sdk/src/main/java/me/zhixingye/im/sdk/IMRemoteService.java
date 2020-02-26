package me.zhixingye.im.sdk;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.sdk.handle.ContactManagerHandle;
import me.zhixingye.im.sdk.handle.ConversationManagerHandle;
import me.zhixingye.im.sdk.handle.GroupManagerHandle;
import me.zhixingye.im.sdk.handle.MessageManagerHandle;
import me.zhixingye.im.sdk.handle.SMSManagerHandle;
import me.zhixingye.im.sdk.handle.StorageManagerHandle;
import me.zhixingye.im.sdk.handle.UserManagerHandle;

public class IMRemoteService extends Service {

    public IMRemoteService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        IMCore.tryInit(this, "111.231.238.209", 9090, "1.0.0");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mIMServiceBinder;
    }

    private final IBinder mIMServiceBinder = new IRemoteService.Stub() {

        @Override
        public IContactManagerHandle getContactManagerHandle() throws RemoteException {
            return new ContactManagerHandle();
        }

        @Override
        public IConversationManagerHandle getConversationManagerHandle() throws RemoteException {
            return new ConversationManagerHandle();
        }

        @Override
        public IGroupManagerHandle getGroupManagerHandle() throws RemoteException {
            return new GroupManagerHandle();
        }

        @Override
        public IMessageManagerHandle getMessageManagerHandle() throws RemoteException {
            return new MessageManagerHandle();
        }

        @Override
        public ISMSManagerHandle getSMSManagerHandle() throws RemoteException {
            return new SMSManagerHandle();
        }

        @Override
        public IStorageManagerHandle getStorageManagerHandle() throws RemoteException {
            return new StorageManagerHandle();
        }

        @Override
        public IUserManagerHandle getUserManagerHandle() throws RemoteException {
            return new UserManagerHandle();
        }
    };
}
