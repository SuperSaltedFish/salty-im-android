package me.zhixingye.im.sdk.handle;


import com.salty.protos.LoginResp;
import com.salty.protos.RegisterResp;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.sdk.IAccountServiceHandle;
import me.zhixingye.im.sdk.IRemoteCallback;

/**
 * Created by zhixingye on 2020年04月29日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class AccountServiceHandle extends IAccountServiceHandle.Stub {
    @Override
    public void registerByTelephone(String telephone, String password, String verificationCode, IRemoteCallback callback) {
        IMCore.get().getAccountService()
                .registerByTelephone(
                        telephone,
                        password,
                        verificationCode,
                        new ByteRemoteCallback<RegisterResp>(callback));
    }

    @Override
    public void registerByEmail(String email, String password, String verificationCode, IRemoteCallback callback) {
        IMCore.get().getAccountService()
                .registerByEmail(
                        email,
                        password,
                        verificationCode,
                        new ByteRemoteCallback<RegisterResp>(callback));
    }

    @Override
    public void loginByTelephone(String telephone, String password, String verificationCode, IRemoteCallback callback) {
        IMCore.get().getAccountService().loginByTelephone(
                telephone,
                password,
                verificationCode,
                new ByteRemoteCallback<LoginResp>(callback));
    }

    @Override
    public void loginByEmail(String email, String password, String verificationCode, IRemoteCallback callback) {
        IMCore.get().getAccountService().loginByEmail(
                email,
                password,
                verificationCode,
                new ByteRemoteCallback<LoginResp>(callback));
    }

    @Override
    public void logout() {
        IMCore.get().getAccountService().logout();
    }

    @Override
    public boolean isLogged() {
        return IMCore.get().getAccountService().isLogged();
    }

    @Override
    public String getCurrentUserId() {
        return IMCore.get().getAccountService().getCurrentUserId();
    }

    @Override
    public String getCurrentUserToken() {
        return IMCore.get().getAccountService().getCurrentUserToken();
    }
}
