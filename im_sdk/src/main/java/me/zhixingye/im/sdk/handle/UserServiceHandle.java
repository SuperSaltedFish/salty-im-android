package me.zhixingye.im.sdk.handle;


import android.os.RemoteException;

import me.zhixingye.im.sdk.IResultCallback;
import me.zhixingye.im.sdk.IUserServiceHandle;

/**
 * Created by zhixingye on 2020年01月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class UserServiceHandle extends IUserServiceHandle.Stub {
    @Override
    public void registerByTelephone(String telephone, String password, String verificationCode, IResultCallback callback) throws RemoteException {

    }

    @Override
    public void loginByTelephone(String telephone, String password, String verificationCode, IResultCallback callback) throws RemoteException {

    }
}
