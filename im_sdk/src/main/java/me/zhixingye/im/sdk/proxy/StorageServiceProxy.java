package me.zhixingye.im.sdk.proxy;

import android.annotation.SuppressLint;

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
        return false;
    }

    @Override
    public String getFromConfigurationPreferences(String key) {
        return null;
    }

    @Override
    public boolean putToUserPreferences(String key, String value) {
        return false;
    }

    @Override
    public String getFromUserPreferences(String key) {
        return null;
    }
}
