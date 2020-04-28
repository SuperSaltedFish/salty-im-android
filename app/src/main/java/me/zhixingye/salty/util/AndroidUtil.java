package me.zhixingye.salty.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by zhixingye on 2020年04月28日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class AndroidUtil {
    public static String getProcessName(Context cxt) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        if (am == null) {
            return null;
        }
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        int currentProcessPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : runningApps) {
            if (info.pid == currentProcessPid) {
                return info.processName;
            }
        }
        return null;
    }
}
