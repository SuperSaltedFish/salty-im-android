package me.zhixingye.im.sdk.proxy;

import com.salty.protos.ObtainSMSCodeReq;
import com.salty.protos.ObtainSMSCodeResp;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.IRemoteService;
import me.zhixingye.im.sdk.ISMSServiceHandle;
import me.zhixingye.im.service.SMSService;
import me.zhixingye.im.tool.Logger;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class SMSServiceProxy implements SMSService, RemoteProxy {

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
    public void obtainVerificationCodeForTelephoneType(String telephone, ObtainSMSCodeReq.CodeType type, RequestCallback<ObtainSMSCodeResp> callback) {
        try {
            mISMSHandle.obtainVerificationCodeForTelephoneType(telephone, type.getNumber(), new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
        }
    }
}
