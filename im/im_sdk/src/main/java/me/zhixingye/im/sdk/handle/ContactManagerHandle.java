package me.zhixingye.im.sdk.handle;

import android.os.RemoteException;

import me.zhixingye.im.sdk.IContactManagerHandle;
import me.zhixingye.im.sdk.IResultCallback;

/**
 * Created by zhixingye on 2020年02月02日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class ContactManagerHandle extends IContactManagerHandle.Stub {
    @Override
    public void requestContact(String userId, String reason, IResultCallback callback) throws RemoteException {
    }

    @Override
    public void refusedContact(String userId, String reason, IResultCallback callback) throws RemoteException {

    }

    @Override
    public void acceptContact(String userId, IResultCallback callback) throws RemoteException {

    }

    @Override
    public void deleteContact(String userId, IResultCallback callback) throws RemoteException {

    }
}
