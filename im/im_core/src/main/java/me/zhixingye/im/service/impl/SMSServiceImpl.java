package me.zhixingye.im.service.impl;

import com.salty.protos.ObtainSMSCodeReq;

import me.zhixingye.im.api.SMSApi;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.ApiService;
import me.zhixingye.im.service.SMSService;

/**
 * Created by zhixingye on 2020年01月10日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class SMSServiceImpl implements SMSService {


    public SMSServiceImpl() {
    }

    @Override
    public void obtainVerificationCodeForTelephoneType(String telephone, ObtainSMSCodeReq.CodeType type, RequestCallback<Void> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(SMSApi.class)
                .obtainVerificationCodeForTelephoneType(telephone, type, callback);
    }

    public void destroy() {

    }
}
