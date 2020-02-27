package me.zhixingye.im.service;

import com.salty.protos.ObtainSMSCodeReq;
import com.salty.protos.ObtainSMSCodeResp;
import com.salty.protos.SMSServiceGrpc;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.tool.CallbackHelper;

/**
 * Created by zhixingye on 2020年01月10日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class SMSService extends BasicService {

    private static volatile SMSService sSMSService;

    public static SMSService get() {
        if (sSMSService == null) {
            synchronized (SMSService.class) {
                if (sSMSService == null) {
                    sSMSService = new SMSService();
                }
            }
        }
        return sSMSService;
    }

    private SMSServiceGrpc.SMSServiceStub mSMSServiceStub;

    private SMSService() {
        super();
        mSMSServiceStub = SMSServiceGrpc.newStub(getChannel());
    }

    public void obtainVerificationCodeForTelephoneType(String telephone, ObtainSMSCodeReq.CodeType type, RequestCallback<Void> callback) {
        ObtainSMSCodeReq smsReq = ObtainSMSCodeReq.newBuilder()
                .setTelephone(telephone)
                .setCodeType(type)
                .build();

        mSMSServiceStub.obtainSMSCode(createReq(smsReq), new DefaultStreamObserver<>(ObtainSMSCodeResp.getDefaultInstance(), new RequestCallback<ObtainSMSCodeResp>() {
            @Override
            public void onCompleted(ObtainSMSCodeResp response) {
                CallbackHelper.callCompleted(null, callback);
            }

            @Override
            public void onFailure(int code, String error) {
                CallbackHelper.callFailure(code, error, callback);
            }
        }));
    }
}
