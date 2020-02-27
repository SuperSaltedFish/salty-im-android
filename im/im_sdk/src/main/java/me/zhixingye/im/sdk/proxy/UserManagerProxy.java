package me.zhixingye.im.sdk.proxy;

import android.os.RemoteException;

import com.salty.protos.GetUserInfoResp;
import com.salty.protos.QueryUserInfoResp;
import com.salty.protos.UpdateUserInfoResp;
import com.salty.protos.UserProfile;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.manager.UserManager;
import me.zhixingye.im.sdk.IUserManagerHandle;
import me.zhixingye.im.sdk.util.CallbackUtil;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class UserManagerProxy extends BasicProxy implements UserManager {

    private IUserManagerHandle mManagerHandle;

    public UserManagerProxy() {
    }

    public void bindHandle(IUserManagerHandle handle) {
        mManagerHandle = handle;
    }

    public void unbindHandle() {
        mManagerHandle = null;
    }


    @Override
    public UserProfile getUserProfile() {
        return null;
    }

    @Override
    public void updateUserInfo(String nickname, String description, UserProfile.Sex sex, long birthday, String location, RequestCallback<UpdateUserInfoResp> callback) {
        if (isServiceUnavailable(mManagerHandle, callback)) {
            return;
        }
        try {
            mManagerHandle.updateUserInfo(nickname, description, sex.getNumber(), birthday, location, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void getUserInfoByUserId(String userId, RequestCallback<GetUserInfoResp> callback) {
        if (isServiceUnavailable(mManagerHandle, callback)) {
            return;
        }
        try {
            mManagerHandle.getUserInfoByUserId(userId, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void queryUserInfoByTelephone(String telephone, RequestCallback<QueryUserInfoResp> callback) {
        if (isServiceUnavailable(mManagerHandle, callback)) {
            return;
        }
        try {
            mManagerHandle.queryUserInfoByTelephone(telephone, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void queryUserInfoByEmail(String email, RequestCallback<QueryUserInfoResp> callback) {
        if (isServiceUnavailable(mManagerHandle, callback)) {
            return;
        }
        try {
            mManagerHandle.queryUserInfoByEmail(email, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }
}