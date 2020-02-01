package me.zhixingye.im.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

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


    private IMClient(Context context) {
        initRemoteIMService(context);
    }

    private void initRemoteIMService(Context context) {
        context.bindService(new Intent(context, IMService.class), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }

            @Override
            public void onBindingDied(ComponentName name) {

            }

            @Override
            public void onNullBinding(ComponentName name) {

            }
        }, Context.BIND_IMPORTANT);
    }
}
