package me.zhixingye.im.sdk.proxy;

import android.os.RemoteException;

import com.salty.protos.LoginResp;
import com.salty.protos.RegisterResp;

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
        if (checkServiceState(mServiceHandle, callback)) {
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
        if (checkServiceState(mServiceHandle, callback)) {
            return;
        }
        try {
            mServiceHandle.loginByTelephone(telephone, password, verificationCode, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }
}