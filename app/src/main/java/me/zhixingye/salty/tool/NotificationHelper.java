package me.zhixingye.salty.tool;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import me.zhixingye.im.tool.Logger;
import me.zhixingye.salty.R;
import me.zhixingye.salty.SaltyApp;
import me.zhixingye.salty.util.AndroidHelper;


/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class NotificationHelper {

    private static final String TAG = "NotificationHelper";

    private static final String CHANNEL_ID_DEFAULT = NotificationHelper.class.getName() + ".Default";
    private static final String CHANNEL_NAME_DEFAULT = "Default";

    private static final String CHANNEL_ID_APP_UPDATE = NotificationHelper.class.getName() + ".AppUpdate";
    private static final String CHANNEL_NAME_APP_UPDATE = "AppUpdate";

    private static final String ACTION_APP_UPDATE = NotificationHelper.class.getName() + ".AppUpdate";

    @SuppressLint("StaticFieldLeak")
    private static NotificationHelper sNotificationHelper;


    public static synchronized NotificationHelper getInstance() {
        if (sNotificationHelper == null) {
            sNotificationHelper = new NotificationHelper(SaltyApp.getAppContext());
        }
        return sNotificationHelper;
    }

    @TargetApi(26)
    private static NotificationChannel getDefaultNotificationChannel(String id, String name) {
        NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(Color.GREEN);
        channel.setVibrationPattern(new long[]{100, 200});
        channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), Notification.AUDIO_ATTRIBUTES_DEFAULT);
        return channel;
    }

    private static Notification.Builder getDefaultNotificationBuilder(Context context, String channelID) {
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(context, channelID);
        } else {
            builder = new Notification.Builder(context);
            builder.setPriority(Notification.PRIORITY_DEFAULT);
        }
        builder
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.ic_launcher);

        return builder;
    }

    private Context mAppContext;
    private Notification.Builder mDefaultNotificationBuilder;
    private Notification.Builder mAppUpdateNotificationBuilder;
    private NotificationManagerCompat mNotificationManager;

    private NotificationHelper(Context appContext) {
        mAppContext = appContext.getApplicationContext();
        mNotificationManager = NotificationManagerCompat.from(mAppContext);
        mDefaultNotificationBuilder = getDefaultNotificationBuilder(mAppContext, CHANNEL_ID_DEFAULT);
        mAppUpdateNotificationBuilder = getDefaultNotificationBuilder(mAppContext, CHANNEL_ID_APP_UPDATE);
        mAppUpdateNotificationBuilder.setOnlyAlertOnce(true);
        mAppUpdateNotificationBuilder.setSound(null, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannel(getDefaultNotificationChannel(CHANNEL_ID_DEFAULT, CHANNEL_NAME_DEFAULT));
            NotificationChannel channel = getDefaultNotificationChannel(CHANNEL_ID_APP_UPDATE, CHANNEL_NAME_APP_UPDATE);
            channel.setSound(null, null);
            channel.setImportance(NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(false);
            mNotificationManager.createNotificationChannel(channel);
        }
        registerReceiver();
    }

    private void registerReceiver() {
        mAppContext.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Uri apkPath = intent.getParcelableExtra(ACTION_APP_UPDATE);
                if (apkPath != null) {
                    AndroidHelper.installAPK(apkPath);
                }
            }
        }, new IntentFilter(ACTION_APP_UPDATE));
    }

    public void showAppUpdateNotification(int percentage, @Nullable Uri apkPath) {
        String title;
        String content;
        Intent intent = null;
        if (percentage == 100) {
            mAppUpdateNotificationBuilder.setProgress(0, 0, false);
            mAppUpdateNotificationBuilder.setOngoing(false);
            intent = new Intent(ACTION_APP_UPDATE);
            intent.putExtra(ACTION_APP_UPDATE, apkPath);
            if (apkPath != null) {
                title = mAppContext.getString(R.string.app_name) + " Download Completed";
                content = "Click to install";
            } else {
                title = mAppContext.getString(R.string.app_name) + " Download Failed";
                content = null;
            }
        } else {
            mAppUpdateNotificationBuilder.setProgress(100, percentage, false);
            mAppUpdateNotificationBuilder.setOngoing(true);
            title = "Application update";
            content = String.format(Locale.getDefault(), "Downloading：%d%%", percentage);
        }
        showNotification(
                mAppUpdateNotificationBuilder,
                CHANNEL_ID_APP_UPDATE.hashCode(),
                title,
                content,
                System.currentTimeMillis(),
                intent,
                false);
    }

    private void showNotification(Notification.Builder builder, int notificationID, String title, String content, long timestamp, Intent contentIntent, boolean isFullScreen) {
        builder
                .setContentTitle(title)
                .setContentText(content)
                .setTicker(title + "：" + content)
                .setWhen(timestamp)
                .setDeleteIntent(null);
        if (contentIntent != null) {
            if (isFullScreen) {
                builder.setFullScreenIntent(PendingIntent.getBroadcast(mAppContext, notificationID, contentIntent, PendingIntent.FLAG_CANCEL_CURRENT), true);
            } else {
                builder.setContentIntent(PendingIntent.getBroadcast(mAppContext, notificationID, contentIntent, PendingIntent.FLAG_CANCEL_CURRENT));
            }
        }
        mNotificationManager.notify(notificationID, builder.build());
    }

    public void cancelNotification(int notificationID) {
        mNotificationManager.cancel(notificationID);
    }

    public void cancelAppUpdateNotification() {
        mNotificationManager.cancel(CHANNEL_ID_APP_UPDATE.hashCode());
    }

    public void cancelAllNotification() {
        mNotificationManager.cancelAll();
    }

    public boolean areNotificationsEnabled() {
        return mNotificationManager.areNotificationsEnabled();
    }

    public void startToNotificationSetting() {
        try {
            // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, mAppContext.getPackageName());
            } else {
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("app_package", mAppContext.getPackageName());
                intent.putExtra("app_uid", mAppContext.getApplicationInfo().uid);
            }
            mAppContext.startActivity(intent);
        } catch (Exception e) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + mAppContext.getPackageName()));
            mAppContext.startActivity(intent);
        }
    }
}

