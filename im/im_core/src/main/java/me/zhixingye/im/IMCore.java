package me.zhixingye.im;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import java.util.Locale;
import java.util.UUID;

import me.zhixingye.im.service.ContactService;
import me.zhixingye.im.service.ConversationService;
import me.zhixingye.im.service.GroupService;
import me.zhixingye.im.service.MessageService;
import me.zhixingye.im.service.SMSService;
import me.zhixingye.im.service.StorageService;
import me.zhixingye.im.service.UserService;
import me.zhixingye.im.service.impl.ContactServiceImpl;
import me.zhixingye.im.service.impl.ConversationServiceImpl;
import me.zhixingye.im.service.impl.GroupServiceImpl;
import me.zhixingye.im.service.impl.MessageServiceImpl;
import me.zhixingye.im.service.impl.NetworkServiceImpl;
import me.zhixingye.im.service.impl.SMSServiceImpl;
import me.zhixingye.im.service.impl.StorageServiceImpl;
import me.zhixingye.im.service.impl.UserServiceImpl;
import me.zhixingye.im.tool.Logger;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class IMCore {

    private static final String TAG = "IMCore";

    private volatile static IMCore sClient;

    public synchronized static void init(Context context, String serverIP, int serverPort, String appVersion) {
        if (sClient != null) {
            throw new RuntimeException("IMCore 已经初始化");
        }
        if (context == null) {
            throw new RuntimeException("context == null");
        }
        sClient = new IMCore(context, serverIP, serverPort, appVersion);
    }

    public synchronized static void release() {
        if (sClient != null) {
            sClient.destroy();
            sClient = null;
        }
    }


    public static IMCore get() {
        if (sClient == null) {
            throw new RuntimeException("IMCore 未初始化");
        }
        return sClient;
    }

    private static final String STORAGE_KEY_DEVICE_ID = "DeviceID";

    private Context mAppContext;

    private String mDeviceID;
    private String mAppVersion;
    private Locale mLanguage;

    private ContactServiceImpl mContactService;
    private ConversationServiceImpl mConversationService;
    private GroupServiceImpl mGroupService;
    private MessageServiceImpl mMessageService;
    private StorageServiceImpl mStorageService;
    private SMSServiceImpl mSMSService;
    private UserServiceImpl mUserService;

    private IMCore(Context context, String serverIP, int serverPort, String version) {
        mAppContext = context.getApplicationContext();
        mAppVersion = version;
        mLanguage = Locale.CHINESE;

        NetworkServiceImpl.init(serverIP, serverPort,mAppContext);

        mContactService = new ContactServiceImpl();
        mConversationService = new ConversationServiceImpl();
        mGroupService = new GroupServiceImpl();
        mMessageService = new MessageServiceImpl();
        mStorageService = new StorageServiceImpl(mAppContext);
        mSMSService = new SMSServiceImpl();
        mUserService = new UserServiceImpl();
    }

    public ContactService getContactService() {
        return mContactService;
    }

    public ConversationService getConversationService() {
        return mConversationService;
    }

    public GroupService getGroupService() {
        return mGroupService;
    }

    public MessageService getMessageService() {
        return mMessageService;
    }

    public StorageService getStorageService() {
        return mStorageService;
    }

    public SMSService getSMSService() {
        return mSMSService;
    }

    public UserService getUserService() {
        return mUserService;
    }

    public String getAppVersion() {
        return mAppVersion;
    }

    public Locale getLanguage() {
        return mLanguage;
    }

    public String getDeviceID() {
        //先从本地拿
        if (TextUtils.isEmpty(mDeviceID)) {
            mDeviceID = mStorageService.getFromConfigurationPreferences(STORAGE_KEY_DEVICE_ID);
        }
        //如果本地没有就初始化一个，目前设备ID的格式是我自己定的，uuid加上一些手机型号版本组成字符串
        if (TextUtils.isEmpty(mDeviceID)) {
//            mDeviceID = Settings.System.getString(mAppContext.getContentResolver(), Settings.Secure.ANDROID_ID);
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

    private void destroy() {
        NetworkServiceImpl.destroy();
        if (mContactService != null) {
            mContactService.destroy();
            mContactService = null;
        }
        if (mConversationService != null) {
            mConversationService.destroy();
            mConversationService = null;
        }
        if (mGroupService != null) {
            mGroupService.destroy();
            mGroupService = null;
        }
        if (mMessageService != null) {
            mMessageService.destroy();
            mMessageService = null;
        }
        if (mStorageService != null) {
            mStorageService.destroy();
            mStorageService = null;
        }
        if (mSMSService != null) {
            mSMSService.destroy();
            mSMSService = null;
        }
        if (mUserService != null) {
            mUserService.destroy();
            mUserService = null;
        }
    }
}
