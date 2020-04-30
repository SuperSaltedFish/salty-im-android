package me.zhixingye.im.sdk.proxy;

import com.salty.protos.ObtainSMSCodeReq;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.IRemoteService;
import me.zhixingye.im.sdk.ISMSServiceHandle;
import me.zhixingye.im.service.SMSService;
import me.zhixingye.im.tool.Logger;

/**
 * Created by zhixingye on 2020年04月29日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class SMSServiceProxy extends BasicProxy implements SMSService {

    private static final String TAG = "ContactServiceProxy";

    private ISMSServiceHandle mISMSHandle;

    @Override
    public void onBindHandle(IRemoteService service) {
        try {
            mISMSHandle = service.getSMSServiceHandle();
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            mISMSHandle = null;
        }
    }

    @Override
    public void obtainVerificationCodeForTelephoneType(String telephone, ObtainSMSCodeReq.CodeType type, RequestCallback<Void> callback) {
        try {
            mISMSHandle.obtainVerificationCodeForTelephoneType(telephone, type.getNumber(), new ResultCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            callRemoteFail(callback);
        }
    }
}
