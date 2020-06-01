package me.zhixingye.im.sdk.handle;


import android.os.RemoteException;

import com.salty.protos.LoginResp;
import com.salty.protos.RegisterResp;
import com.salty.protos.ResetPasswordResp;
import com.salty.protos.UserProfile;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.sdk.IAccountServiceHandle;
import me.zhixingye.im.sdk.IRemoteCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class AccountServiceHandle extends IAccountServiceHandle.Stub {
    @Override
    public void registerByTelephone(String telephone, String password, IRemoteCallback callback) {
        IMCore.get().getAccountService()
                .registerByTelephone(
                        telephone,
                        password,
                        new ByteRemoteCallback<RegisterResp>(callback));
    }

    @Override
    public void registerByEmail(String email, String password, IRemoteCallback callback) {
        IMCore.get().getAccountService()
                .registerByEmail(
                        email,
                        password,
                        new ByteRemoteCallback<RegisterResp>(callback));
    }

    @Override
    public void resetLoginPasswordByEmail(String email, String newPassword, IRemoteCallback callback) throws RemoteException {
        IMCore.get().getAccountService()
                .resetLoginPasswordByEmail(
                        email,
                        newPassword,
                        new ByteRemoteCallback<ResetPasswordResp>(callback));
    }

    @Override
    public void resetLoginPasswordByTelephone(String telephone, String newPassword, IRemoteCallback callback) throws RemoteException {
        IMCore.get().getAccountService()
                .resetLoginPasswordByTelephone(
                        telephone,
                        newPassword,
                        new ByteRemoteCallback<ResetPasswordResp>(callback));
    }

    @Override
    public void loginByTelephone(String telephone, String password, IRemoteCallback callback) {
        IMCore.get().getAccountService().loginByTelephone(
                telephone,
                password,
                new ByteRemoteCallback<LoginResp>(callback));
    }

    @Override
    public void loginByEmail(String email, String password, IRemoteCallback callback) {
        IMCore.get().getAccountService().loginByEmail(
                email,
                password,
                new ByteRemoteCallback<LoginResp>(callback));
    }

    @Override
    public void loginByLastLoginInfo(IRemoteCallback callback) {
        IMCore.get().getAccountService().loginByLastLoginInfo(
                new ByteRemoteCallback<UserProfile>(callback));
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

    @Override
    public byte[] getCurrentUserProfile() {
        UserProfile profile = IMCore.get().getAccountService().getCurrentUserProfile();
        if (profile == null) {
            return null;
        } else {
            return profile.toByteArray();
        }
    }
}
