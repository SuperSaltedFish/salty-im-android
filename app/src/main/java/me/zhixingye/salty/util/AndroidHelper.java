package me.zhixingye.salty.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.ArrayRes;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.RawRes;
import androidx.annotation.StringRes;
import androidx.annotation.StyleableRes;
import androidx.core.content.ContextCompat;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */


public class AndroidHelper {

    private static Context sAppContext;

    private static int sScreenWidth;
    private static int sScreenHeight;
    private static int sScreenDensity;

    public synchronized static void init(Context context) {
        sAppContext = context.getApplicationContext();

        WindowManager manager = (WindowManager) sAppContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        Display display = manager.getDefaultDisplay();
        display.getRealMetrics(dm);
        sScreenWidth = dm.widthPixels;
        sScreenHeight = dm.heightPixels;
        sScreenDensity = dm.densityDpi;
    }

    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = sAppContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = sAppContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public static int getScreenWidth() {
        return sScreenWidth;
    }

    public static int getScreenHeight() {
        return sScreenHeight;
    }

    public static int getScreenDensity() {
        return sScreenDensity;
    }

    public static String getString(@StringRes int resID) {
        return sAppContext.getString(resID);
    }

    public static String[] getStringArray(@ArrayRes int resID) {
        return sAppContext.getResources().getStringArray(resID);
    }


    @ColorInt
    public static int getColor(@ColorRes int resID) {
        return ContextCompat.getColor(sAppContext, resID);
    }

    public static Drawable getDrawable(@DrawableRes int resID) {
        return ContextCompat.getDrawable(sAppContext, resID);
    }

    public static float getDimension(@DimenRes int resID) {
        return sAppContext.getResources().getDimension(resID);
    }

    public static TypedArray obtainStyledAttributes(@StyleableRes int[] resID) {
        return sAppContext.obtainStyledAttributes(resID);
    }


    public static float dip2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, sAppContext.getResources().getDisplayMetrics());
    }


    public static float px2dip(float pxValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pxValue, sAppContext.getResources().getDisplayMetrics());
    }

    public static float px2sp(float pxValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, pxValue, sAppContext.getResources().getDisplayMetrics());
    }


    public static int sp2px(float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, sAppContext.getResources().getDisplayMetrics());

    }

    public static Uri getUriFromDrawableRes(@DrawableRes int id) {
        Resources resources = sAppContext.getResources();
        String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(id) + "/"
                + resources.getResourceTypeName(id) + "/"
                + resources.getResourceEntryName(id);
        return Uri.parse(path);
    }

    public static String getVersionName() {
        PackageManager manager = sAppContext.getPackageManager();
        String name = "0";
        try {
            PackageInfo info = manager.getPackageInfo(sAppContext.getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return name;
    }

    public static void showToast(String content) {
        Toast.makeText(sAppContext, content, Toast.LENGTH_SHORT).show();
    }

    public static boolean isPad() {
        return (sAppContext.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean rawResToLocalFile(@RawRes int resID, String savePath) {
        InputStream inputStream = sAppContext.getResources().openRawResource(resID);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(savePath);
            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static void installAPK(Uri apkPath) {
        if (apkPath == null) {
            return;
        }
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setDataAndType(apkPath, "application/vnd.android.package-archive");
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sAppContext.startActivity(install);
    }


    public static void showKeyboard(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    public static int getThemeColor(Context context, @AttrRes int attr, int defaultColor) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        if (theme.resolveAttribute(attr, typedValue, true)) {
            try {
                ColorStateList colorStateList = context.getResources().getColorStateList(typedValue.resourceId, theme);
                return colorStateList.getDefaultColor();
            } catch (Exception ignored) {
                return typedValue.data;
            }
        } else {
            return defaultColor;
        }
    }

}
