package me.zhixingye.im.sdk.handle;

import android.os.RemoteException;

import me.zhixingye.im.sdk.IResultCallback;
import me.zhixingye.im.sdk.ISMSServiceHandle;

/**
 * Created by zhixingye on 2020年04月29日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class SMSServiceHandle extends ISMSServiceHandle.Stub {
    @Override
    public void obtainVerificationCodeForTelephoneType(String telephone, int codeType, IResultCallback callback) throws RemoteException {
        
    }
}
