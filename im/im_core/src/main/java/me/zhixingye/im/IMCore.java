package me.zhixingye.im;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import com.salty.protos.LoginResp;
import com.salty.protos.ObtainSMSCodeReq;
import com.salty.protos.RegisterResp;
import com.salty.protos.ResetPasswordResp;
import com.salty.protos.UserProfile;

import java.util.Locale;
import java.util.concurrent.Semaphore;

import androidx.annotation.Nullable;
import me.zhixingye.im.api.ApiService;
import me.zhixingye.im.constant.ResponseCode;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.manager.ContactManager;
import me.zhixingye.im.manager.ConversationManager;
import me.zhixingye.im.manager.GroupManager;
import me.zhixingye.im.manager.MessageManager;
import me.zhixingye.im.manager.StorageManager;
import me.zhixingye.im.manager.UserManager;
import me.zhixingye.im.manager.impl.ContactManagerImpl;
import me.zhixingye.im.manager.impl.ConversationManagerImpl;
import me.zhixingye.im.manager.impl.GroupManagerImpl;
import me.zhixingye.im.manager.impl.MessageManagerImpl;
import me.zhixingye.im.manager.impl.StorageManagerImpl;
import me.zhixingye.im.manager.impl.UserManagerImpl;
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

    private Context mAppContext;

    private Locale mLanguage;
    private String mToken;
    private String mAppVersion;

    private ApiService mApiService;

    private UserManagerImpl mUserManager;
    private ContactManagerImpl mContactManager;
    private ConversationManagerImpl mConversationManager;
    private GroupManagerImpl mGroupManager;
    private MessageManagerImpl mMessageManager;
    private StorageManagerImpl mStorageManager;

    private Semaphore mLoginLock;
    private volatile boolean isLogged;

    private IMCore(Context context, String serverIP, int serverPort, String version) {
        mAppContext = context.getApplicationContext();
        mAppVersion = version;
        mLanguage = Locale.CHINESE;

        mLoginLock = new Semaphore(1);

        mApiService = new ApiService(mAppContext, serverIP, serverPort, new ApiService.Adapter() {
            @Override
            public String getDeviceId() {
                return getDeviceID();
            }

            @Override
            public String getToken() {
                return IMCore.this.getToken();
            }

            @Override
            public String getAppVersion() {
                return IMCore.this.getAppVersion();
            }

            @Override
            public Locale getLanguage() {
                return IMCore.this.getLanguage();
            }
        });

    }

    private void tryInit(LoginResp response, RequestCallback<?> callback) {
        String token = response.getToken();
        UserProfile userProfile = response.getProfile();
        if (TextUtils.isEmpty(token) || userProfile == null || TextUtils.isEmpty(userProfile.getUserId())) {
            Logger.e(TAG, "tryInit fail");
            CallbackHelper.callFailure(ResponseCode.INTERNAL_ILLICIT_RESP_DATA, callback);
            return;
        }

        String userId = userProfile.getUserId();
        mToken = token;

        mStorageManager = new StorageManagerImpl(mAppContext, TAG, userId);
        mUserManager = new UserManagerImpl(userProfile, mApiService.getUserApi());
        mContactManager = new ContactManagerImpl(userId, mApiService.getContactApi());
        mConversationManager = new ConversationManagerImpl(userId, mApiService.getConversationApi());
        mGroupManager = new GroupManagerImpl(userId, mApiService.getGroupApi());
        mMessageManager = new MessageManagerImpl(userId, mApiService.getMessageApi());

        CallbackHelper.callCompleted(null, callback);
    }

    private void reset() {
        if (mApiService != null) {
            mApiService.release();
            mApiService = null;
        }
        mContactManager = null;
        mConversationManager = null;
        mGroupManager = null;
        mMessageManager = null;
        mStorageManager = null;
        mUserManager = null;
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

    public UserManager getUserManager() {
        return mUserManager;
    }

    private Locale getLanguage() {
        return mLanguage;
    }

    private String getToken() {
        return mToken;
    }

    private String getDeviceID() {
        return Settings.System.getString(mAppContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private String getAppVersion() {
        return mAppVersion;
    }


    public void obtainTelephoneSMSCode(String telephone, ObtainSMSCodeReq.CodeType type, RequestCallback<Void> callback) {
        mApiService.getSMSApi().obtainVerificationCodeForTelephoneType(telephone, type, callback);
    }

    public void resetLoginPasswordByTelephoneSMS(String telephone, String verificationCode, String newPassword, RequestCallback<ResetPasswordResp> callback) {
        mApiService.getUserApi().resetLoginPasswordByTelephoneSMS(telephone, verificationCode, newPassword, callback);
    }

    public void resetLoginPasswordByEmailSMS(String email, String verificationCode, String newPassword, RequestCallback<ResetPasswordResp> callback) {
        mApiService.getUserApi().resetLoginPasswordByEmailSMS(email, verificationCode, newPassword, callback);
    }

    public void resetLoginPasswordByTelephonePassword(String telephone, String oldPassword, String newPassword, RequestCallback<ResetPasswordResp> callback) {
        mApiService.getUserApi().resetLoginPasswordByTelephonePassword(telephone, oldPassword, newPassword, callback);
    }

    public void resetLoginPasswordByEmailPassword(String email, String oldPassword, String newPassword, RequestCallback<ResetPasswordResp> callback) {
        mApiService.getUserApi().resetLoginPasswordByEmailPassword(email, oldPassword, newPassword, callback);
    }

    public void registerByTelephone(String telephone, String password, String verificationCode, RequestCallback<RegisterResp> callback) {
        mApiService.getUserApi().registerByTelephone(telephone, password, verificationCode, callback);
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
                        isLogged = true;
                        mLoginLock.release();
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

        if (!TextUtils.isEmpty(telephone)) {
            mApiService.getUserApi().loginByTelephone(telephone, password, verificationCode, callbackWrapper);
        } else {
            mApiService.getUserApi().loginByEmail(email, password, verificationCode, callbackWrapper);
        }
    }
}
