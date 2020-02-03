package me.zhixingye.im.sdk.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by zhixingye on 2020年02月03日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class SystemUtils {

    public static String getCurrentProcessName(Context cxt) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        if (am == null) {
            return null;
        }
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo info : runningApps) {
            if (info.pid == android.os.Process.myPid()) {
                return info.processName;
            }
        }
        return null;
    }
}
