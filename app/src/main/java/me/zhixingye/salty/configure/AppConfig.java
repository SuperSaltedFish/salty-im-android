package me.zhixingye.salty.configure;

import me.zhixingye.im.sdk.IMClient;

/**
 * Created by zhixingye on 2020年02月04日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class AppConfig {

    public static void setEverStartedGuide(boolean isStart) {
        IMClient.get().getStorageService().putToConfigurationPreferences("isEverStartedGuide", String.valueOf(isStart));
    }

    public static boolean isEverStartedGuide() {
        return Boolean.getBoolean(IMClient.get().getStorageService().getFromConfigurationPreferences("isEverStartedGuide"));
    }
}
