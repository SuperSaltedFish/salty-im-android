package me.zhixingye.im.sdk;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import me.zhixingye.im.sdk.handle.SMSServiceHandle;
import me.zhixingye.im.sdk.handle.UserServiceHandle;

public class IMService extends Service {

    public IMService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mIMServiceBinder;
    }

    private final IBinder mIMServiceBinder = new IRemoteService.Stub() {

        @Override
        public IUserServiceHandle getUserServiceHandle() throws RemoteException {
            return new UserServiceHandle();
        }

        @Override
        public ISMSServiceHandle getSMSServiceHandle() throws RemoteException {
            return new SMSServiceHandle();
        }
    };
}
