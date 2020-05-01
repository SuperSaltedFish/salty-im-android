package me.zhixingye.im.service.impl;

import com.salty.protos.ObtainSMSCodeReq;
import com.salty.protos.ObtainSMSCodeResp;

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

    @Override
    public void obtainVerificationCodeForTelephoneType(String telephone, ObtainSMSCodeReq.CodeType type, RequestCallback<ObtainSMSCodeResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(SMSApi.class)
                .obtainVerificationCodeForTelephoneType(telephone, type, callback);
    }

    public void destroy() {

    }
}
