package me.zhixingye.im.sdk.proxy;

import android.annotation.SuppressLint;

import me.zhixingye.im.service.StorageService;
import me.zhixingye.im.sdk.IRemoteService;
import me.zhixingye.im.sdk.IStorageServiceHandle;
import me.zhixingye.im.tool.Logger;


/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */

@SuppressLint("ApplySharedPref")
public class StorageServiceProxy implements StorageService, RemoteProxy {

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
