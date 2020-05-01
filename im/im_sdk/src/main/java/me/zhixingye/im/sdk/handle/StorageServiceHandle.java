package me.zhixingye.im.sdk.handle;

import android.os.RemoteException;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.sdk.IStorageServiceHandle;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class StorageServiceHandle extends IStorageServiceHandle.Stub {
    @Override
    public boolean putToConfigurationPreferences(String key, String value)  {
        return IMCore.get().getStorageService().putToConfigurationPreferences(key,value);
    }

    @Override
    public String getFromConfigurationPreferences(String key)  {
        return IMCore.get().getStorageService().getFromConfigurationPreferences(key);
    }

    @Override
    public boolean putToUserPreferences(String key, String value)  {
        return IMCore.get().getStorageService().putToUserPreferences(key,value);
    }

    @Override
    public String getFromUserPreferences(String key)  {
        return IMCore.get().getStorageService().getFromUserPreferences(key);
    }
}
