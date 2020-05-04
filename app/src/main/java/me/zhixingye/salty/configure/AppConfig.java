package me.zhixingye.salty.configure;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class AppConfig {

    public static final int MIN_TELEPHONE_LENGTH = 8;
    public static final int PHONE_VERIFY_CODE_LENGTH = 6;

    public static final String USER_AGREEMENT_URL = "https://josenspire.github.io/super-im/#/";

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
