package me.zhixingye.im.sdk.handle;

import android.os.RemoteException;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.sdk.IStorageServiceHandle;

/**
 * Created by zhixingye on 2020年02月02日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class StorageServiceHandle extends IStorageServiceHandle.Stub {
    @Override
    public boolean putToConfigurationPreferences(String key, String value) throws RemoteException {
        return IMCore.get().getStorageService().putToConfigurationPreferences(key,value);
    }

    @Override
    public String getFromConfigurationPreferences(String key) throws RemoteException {
        return IMCore.get().getStorageService().getFromConfigurationPreferences(key);
    }

    @Override
    public boolean putToUserPreferences(String key, String value) throws RemoteException {
        return IMCore.get().getStorageService().putToUserPreferences(key,value);
    }

    @Override
    public String getFromUserPreferences(String key) throws RemoteException {
        return IMCore.get().getStorageService().getFromUserPreferences(key);
    }
}
