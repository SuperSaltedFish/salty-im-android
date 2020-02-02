package me.zhixingye.im.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * Created by zhixingye on 2020年02月01日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class IMClient {

    private static IMClient sIMClient;

    public synchronized static void init(Context context) {
        if (sIMClient != null) {
            throw new RuntimeException("IMClient 已经初始化");
        }
        context = context.getApplicationContext();
        sIMClient = new IMClient(context);
    }

    public static IMClient get() {
        if (sIMClient == null) {
            throw new RuntimeException("IMClient 未初始化");
        }
        return sIMClient;
    }

    private IRemoteService mRemoteService;
    private IUserServiceHandle mUserServiceHandle;
    private ISMSServiceHandle mSMSServiceHandle;

    private IMClient(Context context) {
        initRemoteIMService(context);
    }

    private void initRemoteIMService(Context context) {
        context.bindService(new Intent(context, IMService.class), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mRemoteService = IRemoteService.Stub.asInterface(service);
                try {
                    mUserServiceHandle = mRemoteService.getUserServiceHandle();
                    mSMSServiceHandle = mRemoteService.getSMSServiceHandle();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mRemoteService = null;
                mUserServiceHandle = null;
                mSMSServiceHandle = null;
            }

            @Override
            public void onBindingDied(ComponentName name) {
                onServiceDisconnected(name);
            }

        }, Context.BIND_AUTO_CREATE);
    }


}
