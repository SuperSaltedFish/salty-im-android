package me.zhixingye.salty;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;


import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import me.jessyan.autosize.AutoSizeConfig;
import me.zhixingye.im.sdk.IMClient;
import me.zhixingye.im.sdk.util.SystemUtils;
import me.zhixingye.salty.configure.AppConfig;
import me.zhixingye.salty.tool.ActivityHelper;
import me.zhixingye.salty.util.AndroidHelper;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class SaltyApp extends Application {

    private static Context sAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = this;

        if (TextUtils.equals(SystemUtils.getCurrentProcessName(this), getPackageName())) {
            IMClient.init(this);

            AppConfig.init(this);
            AndroidHelper.init(this);
            ActivityHelper.init(this);
            setupThirdPart();

        }
    }

    private void setupThirdPart() {
//        EmojiCompat.init(new BundledEmojiCompatConfig(this));
        AutoSizeConfig.getInstance().setDesignWidthInDp(420).setBaseOnWidth(true).setExcludeFontScale(true);

        Beta.autoCheckUpgrade = false;
        Beta.enableHotfix = false;
        Beta.enableNotification = false;
        Beta.autoDownloadOnWifi = false;
        Beta.showInterruptedStrategy = true;
        Bugly.init(this, AppConfig.BUGLY_APP_ID, BuildConfig.DEBUG);
        Beta.checkAppUpgrade(false, true);
    }

    public static Context getAppContext() {
        return sAppContext;
    }

}
