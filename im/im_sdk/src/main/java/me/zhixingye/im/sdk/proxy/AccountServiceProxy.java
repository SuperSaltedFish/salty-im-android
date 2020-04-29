package me.zhixingye.im.sdk.proxy;

import android.os.RemoteException;

import com.salty.protos.LoginResp;
import com.salty.protos.RegisterResp;

import androidx.annotation.Nullable;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.IAccountServiceHandle;
import me.zhixingye.im.sdk.IRemoteService;
import me.zhixingye.im.service.AccountService;

/**
 * Created by zhixingye on 2020年04月29日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class AccountServiceProxy extends BasicProxy implements AccountService {

    private IAccountServiceHandle mAccountHandle;

    public AccountServiceProxy(IMServiceConnector proxy) {
        super(proxy);
    }

    @Override
    protected void onConnectRemoteService(IRemoteService service) {
        try {
            mAccountHandle = service.getAccountServiceHandle();
        } catch (RemoteException e) {
            e.printStackTrace();
            mAccountHandle = null;
        }
    }

    @Override
    public void registerByTelephone(String telephone, String password, String verificationCode, RequestCallback<RegisterResp> callback) {
        try {
            mAccountHandle.registerByTelephone(telephone, password, verificationCode, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerByEmail(String email, String password, String verificationCode, RequestCallback<RegisterResp> callback) {
        try {
            mAccountHandle.registerByEmail(email, password, verificationCode, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loginByTelephone(String telephone, String password, @Nullable String verificationCode, RequestCallback<LoginResp> callback) {
        try {
            mAccountHandle.loginByTelephone(telephone, password, verificationCode, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loginByEmail(String email, String password, @Nullable String verificationCode, RequestCallback<LoginResp> callback) {
        try {
            mAccountHandle.loginByEmail(email, password, verificationCode, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void logout() {
        try {
            mAccountHandle.logout();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getCurrentUserId() {
        try {
            return mAccountHandle.getCurrentUserId();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String getCurrentUserToken() {
        try {
            return mAccountHandle.getCurrentUserToken();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return "";
    }
}
