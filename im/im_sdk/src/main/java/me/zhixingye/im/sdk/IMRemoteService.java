package me.zhixingye.im.sdk;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.sdk.handle.ContactServiceHandle;
import me.zhixingye.im.sdk.handle.ConversationServiceHandle;
import me.zhixingye.im.sdk.handle.GroupServiceHandle;
import me.zhixingye.im.sdk.handle.MessageServiceHandle;
import me.zhixingye.im.sdk.handle.SMSServiceHandle;
import me.zhixingye.im.sdk.handle.StorageServiceHandle;
import me.zhixingye.im.sdk.handle.UserServiceHandle;

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
        IMCore.release();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mIMServiceBinder;
    }

    private final IBinder mIMServiceBinder = new IRemoteService.Stub() {

        @Override
        public IUserServiceHandle getUserServiceHandle() {
            return new UserServiceHandle();
        }

        @Override
        public IContactServiceHandle getContactServiceHandle() {
            return new ContactServiceHandle();
        }

        @Override
        public IConversationServiceHandle getConversationServiceHandle() {
            return new ConversationServiceHandle();
        }

        @Override
        public IGroupServiceHandle getGroupServiceHandle() {
            return new GroupServiceHandle();
        }

        @Override
        public IMessageServiceHandle getMessageServiceHandle() {
            return new MessageServiceHandle();
        }

        @Override
        public ISMSServiceHandle getSMSServiceHandle() {
            return new SMSServiceHandle();
        }

        @Override
        public IStorageServiceHandle getStorageServiceHandle() {
            return new StorageServiceHandle();
        }
    };
}
