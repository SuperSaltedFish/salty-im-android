// IContactServiceHandle.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.IRemoteCallback;

interface IContactServiceHandle {
    void requestContact(String userId, String reason, IRemoteCallback callback);

    void refusedContact(String userId, String reason, IRemoteCallback callback);

    void acceptContact(String userId, IRemoteCallback callback);

    void deleteContact(String userId, IRemoteCallback callback);
}
