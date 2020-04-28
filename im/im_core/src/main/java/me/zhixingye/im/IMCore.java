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
import me.zhixingye.im.service.impl.ApiServiceImpl;
import me.zhixingye.im.constant.ResponseCode;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.ContactService;
import me.zhixingye.im.service.ConversationService;
import me.zhixingye.im.service.GroupService;
import me.zhixingye.im.service.MessageService;
import me.zhixingye.im.service.StorageService;
import me.zhixingye.im.service.UserService;
import me.zhixingye.im.service.impl.ContactServiceImpl;
import me.zhixingye.im.service.impl.ConversationServiceImpl;
import me.zhixingye.im.service.impl.GroupServiceImpl;
import me.zhixingye.im.service.impl.MessageServiceImpl;
import me.zhixingye.im.service.impl.StorageServiceImpl;
import me.zhixingye.im.service.impl.UserServiceImpl;
import me.zhixingye.im.tool.CallbackHelper;
import me.zhixingye.im.tool.Logger;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class IMCore {

    private static final String TAG = "IMCore";

    private volatile static IMCore sClient;

    public synchronized static void tryInit(Context context, String serverAddress, String appVersion) {
        if (sClient != null) {
            throw new RuntimeException("IMCore 已经初始化");
        }
        if (context == null) {
            throw new RuntimeException("context == null");
        }
        sClient = new IMCore(context, serverAddress, appVersion);
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

    private ApiServiceImpl mApiServiceImpl;

    private UserServiceImpl mUserManager;
    private ContactServiceImpl mContactManager;
    private ConversationServiceImpl mConversationManager;
    private GroupServiceImpl mGroupManager;
    private MessageServiceImpl mMessageManager;
    private StorageServiceImpl mStorageManager;

    private Semaphore mLoginLock;
    private volatile boolean isLogged;

    private IMCore(Context context, String serverAddress, String version) {
        mAppContext = context.getApplicationContext();
        mAppVersion = version;
        mLanguage = Locale.CHINESE;

        mLoginLock = new Semaphore(1);

        mStorageManager = new StorageServiceImpl(mAppContext, TAG);
        mUserManager = new UserServiceImpl(mApiServiceImpl.getUserApi());
        mContactManager = new ContactServiceImpl(mApiServiceImpl.getContactApi());
        mConversationManager = new ConversationServiceImpl(mApiServiceImpl.getConversationApi());
        mGroupManager = new GroupServiceImpl(mApiServiceImpl.getGroupApi());
        mMessageManager = new MessageServiceImpl(mApiServiceImpl.getMessageApi());

        mApiServiceImpl = new ApiServiceImpl(mAppContext, serverAddress, new ApiServiceImpl.Adapter() {
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


        CallbackHelper.callCompleted(null, callback);
    }

    private void reset() {
        if (mApiServiceImpl != null) {
            mApiServiceImpl.release();
            mApiServiceImpl = null;
        }
        mContactManager = null;
        mConversationManager = null;
        mGroupManager = null;
        mMessageManager = null;
        mStorageManager = null;
        mUserManager = null;
    }

    public ContactService getContactManager() {
        return mContactManager;
    }

    public ConversationService getConversationManager() {
        return mConversationManager;
    }

    public GroupService getGroupManager() {
        return mGroupManager;
    }

    public MessageService getMessageManager() {
        return mMessageManager;
    }

    public StorageService getStorageManager() {
        return mStorageManager;
    }

    public UserService getUserManager() {
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

    public Context getAppContext() {
        return mAppContext;
    }

    public void obtainTelephoneSMSCode(String telephone, ObtainSMSCodeReq.CodeType type, RequestCallback<Void> callback) {
        mApiServiceImpl.getSMSApi().obtainVerificationCodeForTelephoneType(telephone, type, callback);
    }

    public void resetLoginPasswordByTelephoneSMS(String telephone, String verificationCode, String newPassword, RequestCallback<ResetPasswordResp> callback) {
        mApiServiceImpl.getUserApi().resetLoginPasswordByTelephoneSMS(telephone, verificationCode, newPassword, callback);
    }

    public void resetLoginPasswordByEmailSMS(String email, String verificationCode, String newPassword, RequestCallback<ResetPasswordResp> callback) {
        mApiServiceImpl.getUserApi().resetLoginPasswordByEmailSMS(email, verificationCode, newPassword, callback);
    }

    public void resetLoginPasswordByTelephonePassword(String telephone, String oldPassword, String newPassword, RequestCallback<ResetPasswordResp> callback) {
        mApiServiceImpl.getUserApi().resetLoginPasswordByTelephonePassword(telephone, oldPassword, newPassword, callback);
    }

    public void resetLoginPasswordByEmailPassword(String email, String oldPassword, String newPassword, RequestCallback<ResetPasswordResp> callback) {
        mApiServiceImpl.getUserApi().resetLoginPasswordByEmailPassword(email, oldPassword, newPassword, callback);
    }

    public void registerByTelephone(String telephone, String password, String verificationCode, RequestCallback<RegisterResp> callback) {
        mApiServiceImpl.getUserApi().registerByTelephone(telephone, password, verificationCode, callback);
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
            mApiServiceImpl.getUserApi().loginByTelephone(telephone, password, verificationCode, callbackWrapper);
        } else {
            mApiServiceImpl.getUserApi().loginByEmail(email, password, verificationCode, callbackWrapper);
        }
    }
}
