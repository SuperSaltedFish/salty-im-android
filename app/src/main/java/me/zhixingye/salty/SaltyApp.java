package me.zhixingye.salty;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import java.util.List;

import androidx.emoji.bundled.BundledEmojiCompatConfig;
import androidx.emoji.text.EmojiCompat;
import me.jessyan.autosize.AutoSizeConfig;
import me.zhixingye.salty.tool.ActivityHelper;
import me.zhixingye.salty.util.AndroidHelper;

/**
 * Created by zhixingye on 2020年02月03日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class SaltyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (TextUtils.equals(getProcessName(this), getPackageName())) {
            AndroidHelper.init(this);
            ActivityHelper.init(this);

            setupThirdPart();
        }
    }

    private void setupThirdPart() {
        EmojiCompat.init(new BundledEmojiCompatConfig(this));
        AutoSizeConfig.getInstance().setDesignWidthInDp(420).setBaseOnWidth(true).setExcludeFontScale(true);
    }

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
