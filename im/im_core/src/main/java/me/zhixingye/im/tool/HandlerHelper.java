package me.zhixingye.im.tool;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * Created by zhixingye on 2020年02月26日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class HandlerHelper {

    private static final String TAG = "HandlerHelper";

    private volatile static Handler sUIHandle;
    private volatile static Handler sWorkHandle;

    public static Handler getUIHandler() {
        if (sUIHandle == null) {
            synchronized (HandlerHelper.class) {
                sUIHandle = new Handler(Looper.getMainLooper());
            }
        }
        return sUIHandle;
    }

    public static Handler getWorkHandler() {
        if (sUIHandle == null) {
            synchronized (HandlerHelper.class) {
                HandlerThread thread = new HandlerThread(TAG);
                thread.start();
                sUIHandle = new Handler(thread.getLooper());
            }
        }
        return sUIHandle;
    }
}
