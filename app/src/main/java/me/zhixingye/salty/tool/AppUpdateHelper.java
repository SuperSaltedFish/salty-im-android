package me.zhixingye.salty.tool;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextUtils;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.tencent.bugly.beta.UpgradeInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import me.zhixingye.im.tool.Logger;
import me.zhixingye.salty.util.AndroidHelper;
import me.zhixingye.salty.widget.listener.OnDialogOnlySingleClickListener;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class AppUpdateHelper {

    private static final String TAG = "AppUpdateHelper";
    private static boolean isDownloading;


    public static void showUpdateDialog(Context context, final UpgradeInfo upgradeInfo) {
        if (isDownloading || upgradeInfo == null) {
            return;
        }

        StringBuilder info = new StringBuilder();
        info.append("版本: ").append(upgradeInfo.versionName).append("\n");
        info.append("包大小: ").append(new BigDecimal(upgradeInfo.fileSize / 1024 / 1024).setScale(2, BigDecimal.ROUND_HALF_UP)).append("\n");
        info.append("发布时间: ").append(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(upgradeInfo.publishTime))).append("\n");
        info.append("\n");
        info.append("更新说明: \n").append(upgradeInfo.newFeature);

        new MaterialAlertDialogBuilder(context)
                .setTitle("应用更新")
                .setMessage(info.toString())
                .setNegativeButton("下次", null)
                .setPositiveButton("更新", new OnDialogOnlySingleClickListener() {
                    @Override
                    public void onSingleClick(DialogInterface dialog, int which) {
                        String savePath = DirectoryHelper.getExternalDownloadPath();
                        String fileName = "Salty_" + upgradeInfo.versionName + ".apk";
                        File apk = new File(savePath, fileName);
                        if (apk.exists() && !apk.delete()) {
                            return;
                        }
                        try {
                            if (!apk.createNewFile()) {
                                return;
                            }
                        } catch (IOException e) {
                            Logger.e(TAG, "创建文件失败", e);
                            return;
                        }
                        downloadApk(upgradeInfo.apkUrl, savePath, fileName);
                    }
                })
                .show();

    }

    //下载APK并更新到通知栏
    private static void downloadApk(final String url, final String savePath, final String fileName) {
        new AsyncTask<Void, Integer, String>() {
            @Override
            protected String doInBackground(Void... strings) {
                File apk = new File(savePath, fileName);
                OkHttpClient client = new OkHttpClient();
                client.setConnectTimeout(10, TimeUnit.SECONDS);
                client.setReadTimeout(10, TimeUnit.SECONDS);
                client.setWriteTimeout(10, TimeUnit.SECONDS);
                client.setRetryOnConnectionFailure(false);

                Request request = new Request.Builder().url(url).build();
                Response response;
                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    Logger.e(TAG, "更新失败", e);
                    return null;
                }
                if (!response.isSuccessful()) {
                    return null;
                }

                ResponseBody body = response.body();
                if (body == null) {
                    return null;
                }


                BufferedSink sink;
                try {
                    sink = Okio.buffer(Okio.sink(apk));
                } catch (FileNotFoundException e) {
                    Logger.e(TAG, "文件不存在", e);
                    try {
                        body.close();
                    } catch (IOException ex) {
                        Logger.e(TAG, "close失败", ex);
                    }
                    return null;
                }

                BufferedSource source = null;
                try {
                    Buffer buffer = sink.buffer();
                    source = body.source();
                    long len, total = 0;
                    long maxLen = body.contentLength();
                    long lastUpdateTime = 0;
                    int bufferSize = 128 * 1024;
                    int percentage = 0;
                    while ((len = source.read(buffer, bufferSize)) != -1) {
                        sink.emit();
                        total += len;
                        long nowTime = SystemClock.elapsedRealtime();
                        if (nowTime - lastUpdateTime >= 500) {//500ms更新一次
                            lastUpdateTime = nowTime;
                            int newPercentage = (int) (total * 100 / maxLen);
                            if (percentage != newPercentage) {
                                percentage = newPercentage;
                                publishProgress(percentage);
                            }
                        }
                    }
                } catch (IOException e) {
                    Logger.e(TAG, "下载失败", e);
                } finally {
                    if (source != null) {
                        try {
                            source.close();
                        } catch (IOException e) {
                            Logger.e(TAG, "source.close()", e);
                        }
                    }
                    try {
                        body.close();
                    } catch (IOException e) {
                        Logger.e(TAG, "body.close()", e);
                    }
                    try {
                        sink.close();
                    } catch (IOException e) {
                        Logger.e(TAG, "sink.close()", e);
                    }
                }
                return apk.toString();
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                if (values[0] != 100) {
                    NotificationHelper.getInstance().showAppUpdateNotification(values[0], null);
                }
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                NotificationHelper.getInstance().showAppUpdateNotification(100, null);
                isDownloading = false;
            }

            @Override
            protected void onPostExecute(String result) {
                if (TextUtils.isEmpty(result)) {
                    NotificationHelper.getInstance().showAppUpdateNotification(100, null);
                } else {
                    Uri apkPath;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        apkPath = DirectoryHelper.getUriFromPath(savePath, fileName);
                    } else {
                        apkPath = Uri.fromFile(new File(savePath, fileName));
                    }
                    NotificationHelper.getInstance().showAppUpdateNotification(100, apkPath);
                    AndroidHelper.installAPK(apkPath);
                }
                isDownloading = false;
            }
        }.execute();
        isDownloading = true;
    }

}
