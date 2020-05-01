package me.zhixingye.im.tool;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
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
