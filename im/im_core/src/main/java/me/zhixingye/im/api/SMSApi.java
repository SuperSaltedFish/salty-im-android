package me.zhixingye.im.api;

import com.salty.protos.ObtainSMSCodeReq;
import com.salty.protos.ObtainSMSCodeResp;
import com.salty.protos.SMSServiceGrpc;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.impl.ApiServiceImpl;
import me.zhixingye.im.tool.CallbackHelper;

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
