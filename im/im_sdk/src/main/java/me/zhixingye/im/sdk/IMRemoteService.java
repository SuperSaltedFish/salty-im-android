package me.zhixingye.im.sdk;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.sdk.handle.AccountServiceHandle;
import me.zhixingye.im.sdk.handle.ContactServiceHandle;
import me.zhixingye.im.sdk.handle.ConversationServiceHandle;
import me.zhixingye.im.sdk.handle.GroupServiceHandle;
import me.zhixingye.im.sdk.handle.MessageServiceHandle;
import me.zhixingye.im.sdk.handle.SMSServiceHandle;
import me.zhixingye.im.sdk.handle.StorageServiceHandle;
import me.zhixingye.im.sdk.handle.UserServiceHandle;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class IMRemoteService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        IMCore.tryInit(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Process.killProcess(Process.myPid());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mIMServiceBinder;
    }

    private final IBinder mIMServiceBinder = new IRemoteService.Stub() {

        @Override
        public IAccountServiceHandle getAccountServiceHandle() throws RemoteException {
            return new AccountServiceHandle();
        }

        @Override
        public IContactServiceHandle getContactServiceHandle() throws RemoteException {
            return new ContactServiceHandle();
        }

        @Override
        public IConversationServiceHandle getConversationServiceHandle() throws RemoteException {
            return new ConversationServiceHandle();
        }

        @Override
        public IGroupServiceHandle getGroupServiceHandle() throws RemoteException {
            return new GroupServiceHandle();
        }

        @Override
        public IMessageServiceHandle getMessageServiceHandle() throws RemoteException {
            return new MessageServiceHandle();
        }

        @Override
        public ISMSServiceHandle getSMSServiceHandle() throws RemoteException {
            return new SMSServiceHandle();
        }

        @Override
        public IStorageServiceHandle getStorageServiceHandle() throws RemoteException {
            return new StorageServiceHandle();
        }

        @Override
        public IUserServiceHandle getUserServiceHandle() throws RemoteException {
            return new UserServiceHandle();
        }
    };
}
