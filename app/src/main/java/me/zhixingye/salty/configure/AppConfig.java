package me.zhixingye.salty.configure;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhixingye on 2020年02月04日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class AppConfig {

    private static final String TAG = "AppConfig";

    private static SharedPreferences sConfigPreferences;

    public static void init(Context context) {
        context = context.getApplicationContext();
        sConfigPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
    }

    public static void setEverStartedGuide(boolean isStart) {
        sConfigPreferences.edit().putBoolean("isEverStartedGuide", isStart).apply();
    }

    public static boolean isEverStartedGuide() {
        return sConfigPreferences.getBoolean("isEverStartedGuide", false);
    }
}
