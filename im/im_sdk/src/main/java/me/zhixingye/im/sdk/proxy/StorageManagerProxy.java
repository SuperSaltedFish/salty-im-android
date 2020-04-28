package me.zhixingye.im.sdk.proxy;

import android.annotation.SuppressLint;
import android.os.RemoteException;

import me.zhixingye.im.manager.StorageManager;
import me.zhixingye.im.sdk.IRemoteService;
import me.zhixingye.im.sdk.IStorageManagerHandle;


/**
 * Created by 叶智星 on 2018年09月18日.
 * 每一个不曾起舞的日子，都是对生命的辜负。
 */

@SuppressLint("ApplySharedPref")
public class StorageManagerProxy extends BasicProxy implements StorageManager {

    private IStorageManagerHandle mStorageHandle;

    public StorageManagerProxy(IMServiceConnector proxy) {
        super(proxy);
    }

    @Override
    protected void onConnectRemoteService(IRemoteService service) {
        try {
            mStorageHandle = service.getStorageManagerHandle();
        } catch (RemoteException e) {
            e.printStackTrace();
            mStorageHandle = null;
        }
    }

    @Override
    public boolean putToConfigurationPreferences(String key, String value) {
        try {
            return mStorageHandle.putToConfigurationPreferences(key, value);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getFromConfigurationPreferences(String key) {
        try {
            return mStorageHandle.getFromConfigurationPreferences(key);
        } catch (RemoteException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public boolean putToUserPreferences(String key, String value) {
        try {
            return mStorageHandle.putToUserPreferences(key, value);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getFromUserPreferences(String key) {
        try {
            return mStorageHandle.getFromUserPreferences(key);
        } catch (RemoteException e) {
            e.printStackTrace();
            return "";
        }
    }
}
