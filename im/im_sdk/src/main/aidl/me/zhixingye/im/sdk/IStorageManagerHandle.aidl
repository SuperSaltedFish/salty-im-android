// IStorageManagerHandle.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.IResultCallback;

interface IStorageManagerHandle {
    boolean putToConfigurationPreferences(String key, String value);

    String getFromConfigurationPreferences(String key);

    boolean putToUserPreferences(String key, String value);

    String getFromUserPreferences(String key);
}
