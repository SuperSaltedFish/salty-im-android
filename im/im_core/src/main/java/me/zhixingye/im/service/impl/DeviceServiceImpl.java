package me.zhixingye.im.service.impl;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.service.DeviceService;

/**
 * Created by zhixingye on 2020年04月29日.
 * 每一个不曾起舞的日子 都是对生命的辜负
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
