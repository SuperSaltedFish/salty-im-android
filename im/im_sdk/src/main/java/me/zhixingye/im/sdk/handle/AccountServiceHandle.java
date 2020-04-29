package me.zhixingye.im.sdk.handle;

import android.os.RemoteException;

import me.zhixingye.im.sdk.IAccountServiceHandle;
import me.zhixingye.im.sdk.IResultCallback;

/**
 * Created by zhixingye on 2020年04月29日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class AccountServiceHandle extends IAccountServiceHandle.Stub {
    @Override
    public void registerByTelephone(String telephone, String password, String verificationCode, IResultCallback callback) throws RemoteException {

    }

    @Override
    public void registerByEmail(String email, String password, String verificationCode, IResultCallback callback) throws RemoteException {

    }

    @Override
    public void loginByTelephone(String telephone, String password, String verificationCode, IResultCallback callback) throws RemoteException {

    }

    @Override
    public void loginByEmail(String email, String password, String verificationCode, IResultCallback callback) throws RemoteException {

    }

    @Override
    public void logout() throws RemoteException {

    }

    @Override
    public String getCurrentUserId() throws RemoteException {
        return null;
    }

    @Override
    public String getCurrentUserToken() throws RemoteException {
        return null;
    }
}
