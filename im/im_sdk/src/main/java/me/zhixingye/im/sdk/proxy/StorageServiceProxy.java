package me.zhixingye.im.sdk.proxy;

import android.annotation.SuppressLint;

import me.zhixingye.im.service.StorageService;
import me.zhixingye.im.sdk.IRemoteService;
import me.zhixingye.im.sdk.IStorageServiceHandle;
import me.zhixingye.im.tool.Logger;


/**
 * Created by 叶智星 on 2018年09月18日.
 * 每一个不曾起舞的日子，都是对生命的辜负。
 */

@SuppressLint("ApplySharedPref")
public class StorageServiceProxy extends BasicProxy implements StorageService {

    private static final String TAG = "ContactServiceProxy";

    private IStorageServiceHandle mStorageHandle;

    @Override
    public void onBindHandle(IRemoteService service) {
        try {
            mStorageHandle = service.getStorageServiceHandle();
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            mStorageHandle = null;
        }
    }

    @Override
    public boolean putToConfigurationPreferences(String key, String value) {
        try {
            return mStorageHandle.putToConfigurationPreferences(key, value);
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            return false;
        }
    }

    @Override
    public String getFromConfigurationPreferences(String key) {
        try {
            return mStorageHandle.getFromConfigurationPreferences(key);
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            return "";
        }
    }

    @Override
    public boolean putToUserPreferences(String key, String value) {
        try {
            return mStorageHandle.putToUserPreferences(key, value);
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            return false;
        }
    }

    @Override
    public String getFromUserPreferences(String key) {
        try {
            return mStorageHandle.getFromUserPreferences(key);
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            return "";
        }
    }
}
