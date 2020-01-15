package me.zhixingye.im;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import java.util.Locale;
import java.util.UUID;

import me.zhixingye.im.service.NetworkService;
import me.zhixingye.im.service.SMSService;
import me.zhixingye.im.service.StorageService;
import me.zhixingye.im.service.UserService;
import me.zhixingye.im.tool.Logger;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class IMClient {

    private static final String TAG = "IMClient";

    private volatile static IMClient sClient;

    public synchronized static void init(Context context, String serverIP, int serverPort, String appVersion) {
        if (sClient != null) {
            throw new RuntimeException("IMClient 已经初始化");
        }
        if (context == null) {
            throw new RuntimeException("context == null");
        }
        sClient = new IMClient(context, serverIP, serverPort, appVersion);
    }

    public static IMClient get() {
        if (sClient == null) {
            throw new RuntimeException("IMClient 未初始化");
        }
        return sClient;
    }

    private static final String STORAGE_KEY_DEVICE_ID = "DeviceID";

    private Context mAppContext;

    private String mDeviceID;
    private String mAppVersion;
    private Locale mLanguage;

    private StorageService mStorageService;
    private SMSService mSMSService;
    private UserService mUserService;

    public IMClient(Context context, String serverIP, int serverPort, String version) {
        mAppContext = context.getApplicationContext();
        mAppVersion = version;
        mLanguage = Locale.CHINESE;

        NetworkService.init(serverIP, serverPort);

        mStorageService = new StorageService(mAppContext);
        mSMSService = new SMSService();
        mUserService = new UserService();
    }

    public SMSService getSMSService() {
        return mSMSService;
    }

    public UserService getUserService() {
        return mUserService;
    }

    public String getDeviceID() {
        //先从本地拿
        if (TextUtils.isEmpty(mDeviceID)) {
            mDeviceID = mStorageService.getFromConfigurationPreferences(STORAGE_KEY_DEVICE_ID);
        }
        //如果本地没有就初始化一个，目前设备ID的格式是我自己定的，uuid加上一些手机型号版本组成字符串
        if (TextUtils.isEmpty(mDeviceID)) {
            mDeviceID = String.format(Locale.getDefault(), "%s(%s).%s", Build.BRAND, Build.MODEL, UUID.randomUUID().toString());
            //设备ID如果出现空格就替换成_,一些手机型号信息可能带空格
            mDeviceID = mDeviceID.replaceAll(" ", "_");
            //保存ID到本地
            if (!mStorageService.putToConfigurationPreferences(STORAGE_KEY_DEVICE_ID, mDeviceID)) {
                Logger.w(TAG, "saveDeviceIDToLocal fail");
            }
        }
        return mDeviceID;
    }

    public String getAppVersion() {
        return mAppVersion;
    }

    public Locale getLanguage() {
        return mLanguage;
    }
}
