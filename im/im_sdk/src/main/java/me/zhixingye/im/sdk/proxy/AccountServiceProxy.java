package me.zhixingye.im.sdk.proxy;

import com.salty.protos.LoginResp;
import com.salty.protos.RegisterResp;

import androidx.annotation.Nullable;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.IAccountServiceHandle;
import me.zhixingye.im.sdk.IRemoteService;
import me.zhixingye.im.service.AccountService;
import me.zhixingye.im.tool.Logger;

/**
 * Created by zhixingye on 2020年04月29日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class AccountServiceProxy extends BasicProxy implements AccountService {

    private static final String TAG = "AccountServiceProxy";

    private IAccountServiceHandle mAccountHandle;

    @Override
    public void onBindHandle(IRemoteService service) {
        try {
            mAccountHandle = service.getAccountServiceHandle();
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            mAccountHandle = null;
        }
    }

    @Override
    public void registerByTelephone(String telephone, String password, String verificationCode, RequestCallback<RegisterResp> callback) {
        try {
            mAccountHandle.registerByTelephone(telephone, password, verificationCode, new ResultCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            callRemoteFail(callback);
        }
    }

    @Override
    public void registerByEmail(String email, String password, String verificationCode, RequestCallback<RegisterResp> callback) {
        try {
            mAccountHandle.registerByEmail(email, password, verificationCode, new ResultCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            callRemoteFail(callback);
        }
    }

    @Override
    public void loginByTelephone(String telephone, String password, @Nullable String verificationCode, RequestCallback<LoginResp> callback) {
        try {
            mAccountHandle.loginByTelephone(telephone, password, verificationCode, new ResultCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            callRemoteFail(callback);
        }
    }

    @Override
    public void loginByEmail(String email, String password, @Nullable String verificationCode, RequestCallback<LoginResp> callback) {
        try {
            mAccountHandle.loginByEmail(email, password, verificationCode, new ResultCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            callRemoteFail(callback);
        }
    }

    @Override
    public void logout() {
        try {
            mAccountHandle.logout();
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
        }
    }

    @Override
    public String getCurrentUserId() {
        try {
            return mAccountHandle.getCurrentUserId();
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
        }
        return "";
    }

    @Override
    public String getCurrentUserToken() {
        try {
            return mAccountHandle.getCurrentUserToken();
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
        }
        return "";
    }
}
