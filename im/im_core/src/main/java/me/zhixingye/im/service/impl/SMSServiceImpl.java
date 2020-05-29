package me.zhixingye.im.service.impl;

import com.salty.protos.ObtainTelephoneSMSCodeResp;
import com.salty.protos.SMSOperationType;
import com.salty.protos.VerifyTelephoneSMSCodeResp;

import me.zhixingye.im.api.SMSApi;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.ApiService;
import me.zhixingye.im.service.SMSService;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class SMSServiceImpl implements SMSService {


    public SMSServiceImpl() {
    }

    public void destroy() {

    }

    @Override
    public void obtainVerificationCodeForTelephoneType(String telephone, SMSOperationType type, RequestCallback<ObtainTelephoneSMSCodeResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(SMSApi.class)
                .obtainTelephoneSMSCode(telephone, type, callback);
    }

    @Override
    public void verifyTelephoneSMSCode(String telephone, String smsCode, SMSOperationType type, RequestCallback<VerifyTelephoneSMSCodeResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(SMSApi.class)
                .verifyTelephoneSMSCode(telephone, smsCode, type, callback);
    }
}
