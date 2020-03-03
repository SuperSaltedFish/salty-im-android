package me.zhixingye.im.sdk.handle;

import android.os.RemoteException;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.sdk.IStorageManagerHandle;

/**
 * Created by zhixingye on 2020年02月02日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class StorageManagerHandle extends IStorageManagerHandle.Stub {
    @Override
    public boolean putToConfigurationPreferences(String key, String value) throws RemoteException {
        return IMCore.get().getStorageManager().putToConfigurationPreferences(key,value);
    }

    @Override
    public String getFromConfigurationPreferences(String key) throws RemoteException {
        return IMCore.get().getStorageManager().getFromConfigurationPreferences(key);
    }

    @Override
    public boolean putToUserPreferences(String key, String value) throws RemoteException {
        return IMCore.get().getStorageManager().putToUserPreferences(key,value);
    }

    @Override
    public String getFromUserPreferences(String key) throws RemoteException {
        return IMCore.get().getStorageManager().getFromUserPreferences(key);
    }
}
