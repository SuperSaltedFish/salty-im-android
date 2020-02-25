package me.zhixingye.im.sdk.proxy;

import android.annotation.SuppressLint;
import android.os.RemoteException;

import me.zhixingye.im.sdk.IStorageServiceHandle;


/**
 * Created by 叶智星 on 2018年09月18日.
 * 每一个不曾起舞的日子，都是对生命的辜负。
 */

@SuppressLint("ApplySharedPref")
public class StorageServiceProxy extends BasicProxy implements me.zhixingye.im.service.StorageService {

    private IStorageServiceHandle mServiceHandle;

    public StorageServiceProxy() {

    }

    public void bindHandle(IStorageServiceHandle handle) {
        mServiceHandle = handle;
    }

    public void unbindHandle() {
        mServiceHandle = null;
    }

    @Override
    public boolean putToConfigurationPreferences(String key, String value) {
        if (isServiceUnavailable(mServiceHandle, null)) {
            return false;
        }
        try {
            return mServiceHandle.putToConfigurationPreferences(key, value);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getFromConfigurationPreferences(String key) {
        if (isServiceUnavailable(mServiceHandle, null)) {
            return "";
        }
        try {
            return mServiceHandle.getFromConfigurationPreferences(key);
        } catch (RemoteException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public boolean putToUserPreferences(String key, String value) {
        if (isServiceUnavailable(mServiceHandle, null)) {
            return false;
        }
        try {
            return mServiceHandle.putToUserPreferences(key, value);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getFromUserPreferences(String key) {
        if (isServiceUnavailable(mServiceHandle, null)) {
            return "";
        }
        try {
            return mServiceHandle.getFromUserPreferences(key);
        } catch (RemoteException e) {
            e.printStackTrace();
            return "";
        }
    }
}
