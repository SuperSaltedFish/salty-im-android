package me.zhixingye.im.api;

import com.salty.protos.ObtainSMSCodeReq;
import com.salty.protos.ObtainSMSCodeResp;
import com.salty.protos.SMSServiceGrpc;

import io.grpc.ManagedChannel;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.impl.ApiServiceImpl;
import me.zhixingye.im.tool.CallbackHelper;

/**
 * Created by zhixingye on 2020年01月10日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class SMSApi extends BasicApi {

    private SMSServiceGrpc.SMSServiceStub mSMSServiceStub;

    public SMSApi(ManagedChannel channel) {
        mSMSServiceStub = SMSServiceGrpc.newStub(channel);
    }

    public void obtainVerificationCodeForTelephoneType(String telephone, ObtainSMSCodeReq.CodeType type, RequestCallback<ObtainSMSCodeResp> callback) {
        ObtainSMSCodeReq smsReq = ObtainSMSCodeReq.newBuilder()
                .setTelephone(telephone)
                .setCodeType(type)
                .build();

        mSMSServiceStub.obtainSMSCode(
                createReq(smsReq),
                new DefaultStreamObserver<>(ObtainSMSCodeResp.getDefaultInstance(), callback));
    }
}
