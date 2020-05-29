package me.zhixingye.im.api;

import com.salty.protos.ObtainTelephoneSMSCodeReq;
import com.salty.protos.ObtainTelephoneSMSCodeResp;
import com.salty.protos.SMSOperationType;
import com.salty.protos.SMSServiceGrpc;
import com.salty.protos.VerifyTelephoneSMSCodeReq;
import com.salty.protos.VerifyTelephoneSMSCodeResp;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import me.zhixingye.im.listener.RequestCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class SMSApi extends BasicApi {

    private SMSServiceGrpc.SMSServiceStub mSMSServiceStub;

    @Override
    public void onBindManagedChannel(ManagedChannel channel) {
        mSMSServiceStub = SMSServiceGrpc.newStub(channel)
                .withDeadlineAfter(30, TimeUnit.SECONDS);
    }

    public void obtainTelephoneSMSCode(String telephone, SMSOperationType type, RequestCallback<ObtainTelephoneSMSCodeResp> callback) {
        ObtainTelephoneSMSCodeReq smsReq = ObtainTelephoneSMSCodeReq.newBuilder()
                .setTelephone(telephone)
                .setOperationType(type)
                .build();

        mSMSServiceStub.obtainTelephoneSMSCode(
                createReq(smsReq),
                new DefaultStreamObserver<>(ObtainTelephoneSMSCodeResp.getDefaultInstance(), callback));
    }

    public void verifyTelephoneSMSCode(String telephone, String smsCode, SMSOperationType type, RequestCallback<VerifyTelephoneSMSCodeResp> callback) {
        VerifyTelephoneSMSCodeReq smsReq = VerifyTelephoneSMSCodeReq.newBuilder()
                .setTelephone(telephone)
                .setSmsCode(smsCode)
                .setOperationType(type)
                .build();

        mSMSServiceStub.verifyTelephoneSMSCode(
                createReq(smsReq),
                new DefaultStreamObserver<>(VerifyTelephoneSMSCodeResp.getDefaultInstance(), callback));
    }
}
