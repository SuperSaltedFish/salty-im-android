package me.zhixingye.salty.tool;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import me.zhixingye.im.tool.Logger;
import me.zhixingye.salty.SaltyApp;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class DirectoryHelper {

    private static final String TAG = "DirectoryHelper";

    private static final String EXTERNAL_STORAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/salty/";
    private static final String INTERNAL_STORAGE_PATH = SaltyApp.getAppContext().getFilesDir().getAbsolutePath() + "/Internal/";

    private static final String PATH_IMAGE = "image/";
    private static final String PATH_LOG = "log/";
    private static final String PATH_DOWNLOAD = "download/";
    private static final String PATH_WEB_CACHE = "web_cache/";

    private static String createPathIfDoesNotExist(String path) {
        File file;
        file = new File(path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Logger.e(TAG, "create path fail：" + path);
            }
        }
        return path;
    }

    public static Uri getUriFromPath(String path, @Nullable String fileName) {
        createPathIfDoesNotExist(path);
        File file = new File(path);
        if (!TextUtils.isEmpty(fileName)) {
            path = file.getAbsolutePath() + File.separator + fileName;
        }
        Context context = SaltyApp.getAppContext();
        return FileProvider.getUriForFile(context, context.getPackageName(), new File(path));
    }

    public static String getExternalImagePath() {
        return createPathIfDoesNotExist(EXTERNAL_STORAGE_PATH + PATH_IMAGE);
    }

    public static String getExternalLogPath() {
        return createPathIfDoesNotExist(EXTERNAL_STORAGE_PATH + PATH_LOG);
    }

    public static String getExternalDownloadPath() {
        return createPathIfDoesNotExist(EXTERNAL_STORAGE_PATH + PATH_DOWNLOAD);
    }

    public static String getInternalWebCachePath() {
        return createPathIfDoesNotExist(INTERNAL_STORAGE_PATH + PATH_WEB_CACHE);
    }
}
