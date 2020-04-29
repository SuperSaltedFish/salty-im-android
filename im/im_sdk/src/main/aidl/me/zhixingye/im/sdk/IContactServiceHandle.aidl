// IContactServiceHandle.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.IResultCallback;

interface IContactServiceHandle {
    void requestContact(String userId, String reason, IResultCallback callback);

    void refusedContact(String userId, String reason, IResultCallback callback);

    void acceptContact(String userId, IResultCallback callback);

    void deleteContact(String userId, IResultCallback callback);
}
