package me.zhixingye.im;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import com.salty.protos.LoginResp;
import com.salty.protos.RegisterResp;
import com.salty.protos.UserProfile;

import java.util.Locale;
import java.util.concurrent.Semaphore;

import androidx.annotation.Nullable;
import me.zhixingye.im.constant.ResponseCode;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.manager.ContactManager;
import me.zhixingye.im.manager.ConversationManager;
import me.zhixingye.im.manager.GroupManager;
import me.zhixingye.im.manager.MessageManager;
import me.zhixingye.im.manager.SMSManager;
import me.zhixingye.im.manager.StorageManager;
import me.zhixingye.im.manager.UserManager;
import me.zhixingye.im.manager.impl.ContactManagerImpl;
import me.zhixingye.im.manager.impl.ConversationManagerImpl;
import me.zhixingye.im.manager.impl.GroupManagerImpl;
import me.zhixingye.im.manager.impl.MessageManagerImpl;
import me.zhixingye.im.manager.impl.SMSManagerImpl;
import me.zhixingye.im.manager.impl.StorageManagerImpl;
import me.zhixingye.im.manager.impl.UserManagerImpl;
import me.zhixingye.im.service.NetworkService;
import me.zhixingye.im.service.UserService;
import me.zhixingye.im.tool.CallbackHelper;
import me.zhixingye.im.tool.Logger;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class IMCore {

    private static final String TAG = "IMCore";

    private volatile static IMCore sClient;

    public synchronized static void tryInit(Context context, String serverIP, int serverPort, String appVersion) {
        if (sClient != null) {
            throw new RuntimeException("IMCore 已经初始化");
        }
        if (context == null) {
            throw new RuntimeException("context == null");
        }
        sClient = new IMCore(context, serverIP, serverPort, appVersion);
    }

    public static IMCore get() {
        if (sClient == null) {
            throw new RuntimeException("IMCore 未初始化");
        }
        return sClient;
    }

    private static final String STORAGE_KEY_DEVICE_ID = "DeviceID";

    private Context mAppContext;

    private Locale mLanguage;
    private String mToken;
    private String mDeviceID;
    private String mAppVersion;

    private ContactManagerImpl mContactManager;
    private ConversationManagerImpl mConversationManager;
    private GroupManagerImpl mGroupManager;
    private MessageManagerImpl mMessageManager;
    private StorageManagerImpl mStorageManager;
    private SMSManagerImpl mSMSManager;
    private UserManagerImpl mUserManager;

    private Semaphore mLoginLock;
    private volatile boolean isLogged;

    private IMCore(Context context, String serverIP, int serverPort, String version) {
        mAppContext = context.getApplicationContext();
        mAppVersion = version;
        mLanguage = Locale.CHINESE;

        NetworkService.init(mAppContext, serverIP, serverPort, new NetworkService.Adapter() {
            @Override
            public String getDeviceId() {
                return getDeviceID();
            }

            @Override
            public String getToken() {
                return mToken;
            }

            @Override
            public String getAppVersion() {
                return mAppVersion;
            }

            @Override
            public Locale getLanguage() {
                return mLanguage;
            }
        });
    }

    public void registerByTelephone(String telephone, String password, String verificationCode, RequestCallback<RegisterResp> callback) {
        UserService.get().registerByTelephone(telephone, password, verificationCode, callback);
    }

    public void loginByTelephone(String telephone, String password, @Nullable String verificationCode, RequestCallback<LoginResp> callback) {
        login(telephone, null, password, verificationCode, callback);
    }

    public void loginByEmail(String email, String password, @Nullable String verificationCode, RequestCallback<LoginResp> callback) {
        login(null, email, password, verificationCode, callback);
    }

    private void login(String telephone, String email, String password, @Nullable String verificationCode, RequestCallback<LoginResp> callback) {
        try {
            mLoginLock.acquire();
        } catch (InterruptedException e) {
            return;
        }
        if (isLogged) {
            mLoginLock.release();
            throw new RuntimeException("The user has already logged in, please do not log in again！");
        }

        RequestCallback<LoginResp> callbackWrapper = new RequestCallback<LoginResp>() {
            @Override
            public void onCompleted(LoginResp loginResp) {
                tryInit(loginResp, new RequestCallback() {
                    @Override
                    public void onCompleted(Object response) {
                        CallbackHelper.callCompleted(loginResp, callback);
                    }

                    @Override
                    public void onFailure(int code, String error) {
                        callFailure(code, error);
                    }
                });

            }

            @Override
            public void onFailure(int code, String error) {
                callFailure(code, error);
            }

            private void callFailure(int code, String error) {
                reset();
                mLoginLock.release();
                CallbackHelper.callFailure(code, error, callback);
            }
        };

        if (TextUtils.isEmpty(telephone)) {
            UserService.get().loginByTelephone(telephone, password, verificationCode, callbackWrapper);
        } else {
            UserService.get().loginByEmail(email, password, verificationCode, callbackWrapper);
        }
    }

    private void tryInit(LoginResp response, RequestCallback<?> callback) {
        String token = response.getToken();
        UserProfile userProfile = response.getProfile();
        if (TextUtils.isEmpty(token) || userProfile == null || TextUtils.isEmpty(userProfile.getUserId())) {
            Logger.e(TAG, "tryInit fail");
            CallbackHelper.callFailure(ResponseCode.INTERNAL_ILLICIT_RESP_DATA, callback);
            return;
        }

        mToken = token;
        mStorageManager = new StorageManagerImpl(mAppContext, TAG, userProfile.getUserId());
        mUserManager = new UserManagerImpl(userProfile);
        mContactManager = new ContactManagerImpl();
        mConversationManager = new ConversationManagerImpl();
        mGroupManager = new GroupManagerImpl();
        mMessageManager = new MessageManagerImpl();
        mSMSManager = new SMSManagerImpl();

        CallbackHelper.callCompleted(null, callback);
    }

    private void reset() {
        NetworkService.destroy();
        if (mContactManager != null) {
            mContactManager.destroy();
            mContactManager = null;
        }
        if (mConversationManager != null) {
            mConversationManager.destroy();
            mConversationManager = null;
        }
        if (mGroupManager != null) {
            mGroupManager.destroy();
            mGroupManager = null;
        }
        if (mMessageManager != null) {
            mMessageManager.destroy();
            mMessageManager = null;
        }
        if (mStorageManager != null) {
            mStorageManager.destroy();
            mStorageManager = null;
        }
        if (mSMSManager != null) {
            mSMSManager.destroy();
            mSMSManager = null;
        }
        if (mUserManager != null) {
            mUserManager.destroy();
            mUserManager = null;
        }
    }

    public ContactManager getContactManager() {
        return mContactManager;
    }

    public ConversationManager getConversationManager() {
        return mConversationManager;
    }

    public GroupManager getGroupManager() {
        return mGroupManager;
    }

    public MessageManager getMessageManager() {
        return mMessageManager;
    }

    public StorageManager getStorageManager() {
        return mStorageManager;
    }

    public SMSManager getSMSManager() {
        return mSMSManager;
    }

    public UserManager getUserManager() {
        return mUserManager;
    }

    private String getDeviceID() {
        if (TextUtils.isEmpty(mDeviceID)) {
            mDeviceID = mStorageManager.getFromConfigurationPreferences(STORAGE_KEY_DEVICE_ID);
        }
        if (TextUtils.isEmpty(mDeviceID)) {
            mDeviceID = Settings.System.getString(mAppContext.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (!mStorageManager.putToConfigurationPreferences(STORAGE_KEY_DEVICE_ID, mDeviceID)) {
                Logger.w(TAG, "saveDeviceIDToLocal fail");
            }
        }
        return mDeviceID;
    }


}
