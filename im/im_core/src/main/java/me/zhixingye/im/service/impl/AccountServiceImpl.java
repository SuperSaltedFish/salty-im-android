package me.zhixingye.im.service.impl;

import android.text.TextUtils;

import com.salty.protos.LoginResp;
import com.salty.protos.RegisterResp;
import com.salty.protos.ResetPasswordResp;
import com.salty.protos.UserProfile;

import java.util.concurrent.Semaphore;

import androidx.annotation.Nullable;
import me.zhixingye.im.IMCore;
import me.zhixingye.im.api.UserApi;
import me.zhixingye.im.constant.ResponseCode;
import me.zhixingye.im.database.UserDao;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.AccountService;
import me.zhixingye.im.service.ApiService;
import me.zhixingye.im.service.SQLiteService;
import me.zhixingye.im.service.StorageService;
import me.zhixingye.im.tool.CallbackHelper;
import me.zhixingye.im.tool.Logger;
import me.zhixingye.im.util.MD5Util;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class AccountServiceImpl implements AccountService {

    private static final String TAG = "AccountServiceImpl";

    private static final int DATABASE_VERSION = 1;

    private static final String STORAGE_KEY_USER_ID = TAG + ".UserId";
    private static final String STORAGE_KEY_TOKEN = TAG + ".Token";

    private String mUserId;
    private String mToken;

    private Semaphore mLoginLock;

    private volatile boolean isLogged;

    public AccountServiceImpl() {
        mLoginLock = new Semaphore(1);
    }

    @Override
    public void registerByTelephone(String telephone, String password, String smsCode, RequestCallback<RegisterResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(UserApi.class)
                .registerByTelephone(telephone, password, smsCode, callback);
    }

    @Override
    public void registerByEmail(String email, String password, String smsCode, RequestCallback<RegisterResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(UserApi.class)
                .registerByEmail(email, password, smsCode, callback);
    }

    @Override
    public void resetLoginPasswordByTelephoneOldPassword(String telephone, String oldPassword, String newPassword, RequestCallback<ResetPasswordResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(UserApi.class)
                .resetLoginPasswordByTelephoneOldPassword(telephone, oldPassword, newPassword, callback);
    }

    @Override
    public void resetLoginPasswordByTelephoneSMS(String telephone, String smsCode, String newPassword, RequestCallback<ResetPasswordResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(UserApi.class)
                .resetLoginPasswordByTelephoneSMS(telephone, smsCode, newPassword, callback);
    }

    @Override
    public void loginByTelephone(String telephone, String password, @Nullable String smsCode, RequestCallback<LoginResp> callback) {
        login(telephone, null, password, smsCode, callback);
    }

    @Override
    public void loginByEmail(String email, String password, @Nullable String smsCode, RequestCallback<LoginResp> callback) {
        login(null, email, password, smsCode, callback);
    }

    private void login(String telephone, String email, String password, @Nullable String verificationCode, final RequestCallback<LoginResp> callback) {
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
                UserProfile profile = loginResp.getProfile();
                String userId = profile.getUserId();
                String token = loginResp.getToken();

                ServiceAccessor
                        .get(StorageService.class)
                        .putToConfigurationPreferences(STORAGE_KEY_USER_ID, userId);

                ServiceAccessor
                        .get(StorageService.class)
                        .putToConfigurationPreferences(STORAGE_KEY_TOKEN, token);

                SQLiteServiceImpl service = new SQLiteServiceImpl(
                        IMCore.getAppContext(),
                        MD5Util.encrypt16(userId),
                        DATABASE_VERSION);

                UserDao dao = service.createDao(UserDao.class);
                if (!dao.replace(profile)) {
                    Logger.e(TAG, "login fail:replace fail");
                    service.close();
                    onFailure(
                            ResponseCode.INTERNAL_UNKNOWN.getCode(),
                            ResponseCode.INTERNAL_UNKNOWN.getMsg());
                    return;
                }
                ServiceAccessor.register(SQLiteService.class, service);

                mUserId = userId;
                mToken = token;

                isLogged = true;
                mLoginLock.release();
                if (callback != null) {
                    callback.onCompleted(loginResp);
                }
            }

            @Override
            public void onFailure(int code, String error) {
                mLoginLock.release();
                CallbackHelper.callFailure(code, error, callback);
            }

        };

        if (!TextUtils.isEmpty(telephone)) {
            ServiceAccessor.get(ApiService.class)
                    .createApi(UserApi.class)
                    .loginByTelephone(telephone, password, verificationCode, callbackWrapper);
        } else {
            ServiceAccessor.get(ApiService.class)
                    .createApi(UserApi.class)
                    .loginByEmail(email, password, verificationCode, callbackWrapper);
        }
    }

    @Override
    public void logout() {
        ServiceAccessor.get(ApiService.class)
                .createApi(UserApi.class)
                .logout(null);

        SQLiteServiceImpl service = (SQLiteServiceImpl) ServiceAccessor.get(SQLiteService.class);
        if (service != null) {
            service.close();
            ServiceAccessor.register(SQLiteService.class, null);
        }
        isLogged = false;
        mUserId = null;
        mToken = null;
    }

    @Override
    public boolean isLogged() {
        return !TextUtils.isEmpty(mUserId) && !TextUtils.isEmpty(mToken);
    }

    @Override
    public String getCurrentUserId() {
        return mUserId;
    }

    @Override
    public String getCurrentUserToken() {
        return mToken;
    }
}
