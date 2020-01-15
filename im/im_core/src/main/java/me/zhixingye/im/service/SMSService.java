package me.zhixingye.im.service;

import com.salty.protos.SMSReq;
import com.salty.protos.SMSResp;
import com.salty.protos.SMSServiceGrpc;

import me.zhixingye.im.listener.RequestCallback;

/**
 * Created by zhixingye on 2020年01月10日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class SMSService extends BasicService {

    private SMSServiceGrpc.SMSServiceStub mSMSServiceStub;

    public SMSService() {
        super();
        mSMSServiceStub = SMSServiceGrpc.newStub(getChannel());
    }

    public void obtainVerificationCodeForTelephoneType(String telephone, SMSReq.CodeType type, RequestCallback<SMSResp> callback) {
        SMSReq smsReq = SMSReq.newBuilder()
                .setTelephone(telephone)
                .setCodeType(type)
                .build();
        mSMSServiceStub.obtainSMSCode(createReq(smsReq), new DefaultStreamObserver<>(SMSResp.getDefaultInstance(), callback));
    }

    public void obtainVerificationCodeForMailType(String email, SMSReq.CodeType type, RequestCallback<SMSResp> callback) {
        SMSReq smsReq = SMSReq.newBuilder()
                .setEmail(email)
                .setCodeType(type)
                .build();
        mSMSServiceStub.obtainSMSCode(createReq(smsReq), new DefaultStreamObserver<>(SMSResp.getDefaultInstance(), callback));
    }
}
