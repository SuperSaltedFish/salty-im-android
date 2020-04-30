package me.zhixingye.salty;

import android.app.Application;
import android.text.TextUtils;


import androidx.emoji.bundled.BundledEmojiCompatConfig;
import androidx.emoji.text.EmojiCompat;
import me.jessyan.autosize.AutoSizeConfig;
import me.zhixingye.im.sdk.IMClient;
import me.zhixingye.im.sdk.util.SystemUtils;
import me.zhixingye.salty.configure.AppConfig;
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

        if (TextUtils.equals(SystemUtils.getCurrentProcessName(this), getPackageName())) {
            AppConfig.init(this);
            AndroidHelper.init(this);
            ActivityHelper.init(this);
            setupThirdPart();

            IMClient.init(this);
        }
    }

    private void setupThirdPart() {
        EmojiCompat.init(new BundledEmojiCompatConfig(this));
        AutoSizeConfig.getInstance().setDesignWidthInDp(420).setBaseOnWidth(true).setExcludeFontScale(true);
    }

}
