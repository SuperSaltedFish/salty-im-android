package me.zhixingye.im.sdk.proxy;

import android.os.RemoteException;

import com.salty.protos.ObtainSMSCodeReq;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.IRemoteService;
import me.zhixingye.im.sdk.ISMSServiceHandle;
import me.zhixingye.im.service.SMSService;

/**
 * Created by zhixingye on 2020年04月29日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class SMSServiceProxy extends BasicProxy implements SMSService {

    private ISMSServiceHandle mISMSHandle;

    public SMSServiceProxy(IMServiceConnector proxy) {
        super(proxy);
    }

    @Override
    protected void onConnectRemoteService(IRemoteService service) {
        try {
            mISMSHandle = service.getSMSServiceHandle();
        } catch (RemoteException e) {
            e.printStackTrace();
            mISMSHandle = null;
        }
    }

    @Override
    public void obtainVerificationCodeForTelephoneType(String telephone, ObtainSMSCodeReq.CodeType type, RequestCallback<Void> callback) {
        try {
            mISMSHandle.obtainVerificationCodeForTelephoneType(telephone, type.getNumber(), new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
