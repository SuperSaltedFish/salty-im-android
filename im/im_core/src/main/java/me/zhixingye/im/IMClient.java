package me.zhixingye.im;

import android.content.Context;

import java.util.Locale;

import me.zhixingye.im.service.SMSService;
import me.zhixingye.im.service.StorageService;
import me.zhixingye.im.service.UserService;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class IMClient {

    private volatile static IMClient sClient;

    public synchronized static void init(Context context, String appVersion) {
        if (sClient != null) {
            throw new RuntimeException("IMClient 已经初始化");
        }
        if (context == null) {
            throw new RuntimeException("context == null");
        }
        sClient = new IMClient(context, appVersion);
    }

    public static IMClient get() {
        if (sClient == null) {
            throw new RuntimeException("IMClient 未初始化");
        }
        return sClient;
    }

    private Context mAppContext;

    private String mDeviceID;
    private String mAppVersion;
    private Locale mLanguage;

    private StorageService mStorageService;
    private SMSService mSMSService;
    private UserService mUserService;

    public IMClient(Context context, String version) {

        mAppContext = context.getApplicationContext();
        mAppVersion = version;
        mLanguage = Locale.CHINESE;

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
        return mDeviceID;
    }

    public String getAppVersion() {
        return mAppVersion;
    }

    public Locale getLanguage() {
        return mLanguage;
    }
}
