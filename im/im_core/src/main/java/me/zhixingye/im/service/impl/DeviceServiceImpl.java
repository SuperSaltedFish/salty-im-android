package me.zhixingye.im.service.impl;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.service.DeviceService;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class DeviceServiceImpl implements DeviceService {
    @Override
    public String getAppVersion() {
        Context context = IMCore.getAppContext();
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    @Override
    public String getDeviceId() {
        return Settings.System.getString(
                IMCore.getAppContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }
}
