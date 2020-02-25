package me.zhixingye.im.sdk.proxy;

import android.os.RemoteException;

import com.salty.protos.GetUserInfoResp;
import com.salty.protos.LoginResp;
import com.salty.protos.QueryUserInfoResp;
import com.salty.protos.RegisterResp;
import com.salty.protos.ResetPasswordResp;
import com.salty.protos.UpdateUserInfoResp;
import com.salty.protos.UserProfile;

import androidx.annotation.Nullable;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.IUserServiceHandle;
import me.zhixingye.im.sdk.util.CallbackUtil;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class UserServiceProxy extends BasicProxy implements me.zhixingye.im.service.UserService {

    private IUserServiceHandle mServiceHandle;

    public UserServiceProxy() {
    }

    public void bindHandle(IUserServiceHandle handle) {
        mServiceHandle = handle;
    }

    public void unbindHandle() {
        mServiceHandle = null;
    }

    @Override
    public void registerByTelephone(String telephone, String password, String verificationCode, RequestCallback<RegisterResp> callback) {
        if (isServiceUnavailable(mServiceHandle, callback)) {
            return;
        }
        try {
            mServiceHandle.registerByTelephone(telephone, password, verificationCode, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void loginByTelephone(String telephone, String password, @Nullable String verificationCode, RequestCallback<LoginResp> callback) {
        if (isServiceUnavailable(mServiceHandle, callback)) {
            return;
        }
        try {
            mServiceHandle.loginByTelephone(telephone, password, verificationCode, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void resetLoginPasswordByTelephone(String telephone, String verificationCode, String newPassword, RequestCallback<ResetPasswordResp> callback) {
        if (isServiceUnavailable(mServiceHandle, callback)) {
            return;
        }
        try {
            mServiceHandle.resetLoginPasswordByTelephone(telephone, verificationCode, newPassword, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void resetLoginPassword(String telephone, String oldPassword, String newPassword, RequestCallback<ResetPasswordResp> callback) {
        if (isServiceUnavailable(mServiceHandle, callback)) {
            return;
        }
        try {
            mServiceHandle.resetLoginPassword(telephone, oldPassword, newPassword, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void updateUserInfo(String nickname, String description, UserProfile.Sex sex, long birthday, String location, RequestCallback<UpdateUserInfoResp> callback) {
        if (isServiceUnavailable(mServiceHandle, callback)) {
            return;
        }
        try {
            mServiceHandle.updateUserInfo(nickname, description, sex.getNumber(), birthday, location, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void getUserInfoByUserId(String userId, RequestCallback<GetUserInfoResp> callback) {
        if (isServiceUnavailable(mServiceHandle, callback)) {
            return;
        }
        try {
            mServiceHandle.getUserInfoByUserId(userId, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void queryUserInfoByTelephone(String telephone, RequestCallback<QueryUserInfoResp> callback) {
        if (isServiceUnavailable(mServiceHandle, callback)) {
            return;
        }
        try {
            mServiceHandle.queryUserInfoByTelephone(telephone, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void queryUserInfoByEmail(String email, RequestCallback<QueryUserInfoResp> callback) {
        if (isServiceUnavailable(mServiceHandle, callback)) {
            return;
        }
        try {
            mServiceHandle.queryUserInfoByEmail(email, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }
}