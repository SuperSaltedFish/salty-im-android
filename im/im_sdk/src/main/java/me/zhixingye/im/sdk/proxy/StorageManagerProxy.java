package me.zhixingye.im.sdk.proxy;

import android.annotation.SuppressLint;
import android.os.RemoteException;

import me.zhixingye.im.manager.StorageManager;
import me.zhixingye.im.sdk.IStorageManagerHandle;


/**
 * Created by 叶智星 on 2018年09月18日.
 * 每一个不曾起舞的日子，都是对生命的辜负。
 */

@SuppressLint("ApplySharedPref")
public class StorageManagerProxy extends BasicProxy implements StorageManager {

    private IStorageManagerHandle mManagerHandle;

    public StorageManagerProxy() {

    }

    public void bindHandle(IStorageManagerHandle handle) {
        mManagerHandle = handle;
    }

    public void unbindHandle() {
        mManagerHandle = null;
    }

    @Override
    public boolean putToConfigurationPreferences(String key, String value) {
        if (isServiceUnavailable(mManagerHandle, null)) {
            return false;
        }
        try {
            return mManagerHandle.putToConfigurationPreferences(key, value);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getFromConfigurationPreferences(String key) {
        if (isServiceUnavailable(mManagerHandle, null)) {
            return "";
        }
        try {
            return mManagerHandle.getFromConfigurationPreferences(key);
        } catch (RemoteException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public boolean putToUserPreferences(String key, String value) {
        if (isServiceUnavailable(mManagerHandle, null)) {
            return false;
        }
        try {
            return mManagerHandle.putToUserPreferences(key, value);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getFromUserPreferences(String key) {
        if (isServiceUnavailable(mManagerHandle, null)) {
            return "";
        }
        try {
            return mManagerHandle.getFromUserPreferences(key);
        } catch (RemoteException e) {
            e.printStackTrace();
            return "";
        }
    }
}
