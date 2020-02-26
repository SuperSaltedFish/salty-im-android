package me.zhixingye.im.sdk.handle;


import android.os.RemoteException;

import me.zhixingye.im.sdk.IResultCallback;
import me.zhixingye.im.sdk.IUserManagerHandle;

/**
 * Created by zhixingye on 2020年01月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class UserManagerHandle extends IUserManagerHandle.Stub {
    @Override
    public void registerByTelephone(String telephone, String password, String verificationCode, IResultCallback callback) throws RemoteException {

    }

    @Override
    public void loginByTelephone(String telephone, String password, String verificationCode, IResultCallback callback) throws RemoteException {

    }

    @Override
    public void loginByEmail(String email, String password, String verificationCode, IResultCallback callback) throws RemoteException {

    }

    @Override
    public void resetLoginPasswordByTelephone(String telephone, String verificationCode, String newPassword, IResultCallback callback) throws RemoteException {

    }

    @Override
    public void resetLoginPassword(String telephone, String oldPassword, String newPassword, IResultCallback callback) throws RemoteException {

    }

    @Override
    public void updateUserInfo(String nickname, String description, int sex, long birthday, String location, IResultCallback callback) throws RemoteException {

    }

    @Override
    public void getUserInfoByUserId(String userId, IResultCallback callback) throws RemoteException {

    }

    @Override
    public void queryUserInfoByTelephone(String telephone, IResultCallback callback) throws RemoteException {

    }

    @Override
    public void queryUserInfoByEmail(String email, IResultCallback callback) throws RemoteException {

    }
}
