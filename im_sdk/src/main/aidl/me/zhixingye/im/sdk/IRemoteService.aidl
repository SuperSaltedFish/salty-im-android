// IRemoteService.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.IUserServiceHandle;
import me.zhixingye.im.sdk.ISMSServiceHandle;

interface IRemoteService {
    IUserServiceHandle getUserServiceHandle();

    ISMSServiceHandle getSMSServiceHandle();
}
