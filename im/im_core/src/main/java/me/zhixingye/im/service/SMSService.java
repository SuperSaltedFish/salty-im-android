package me.zhixingye.im.service;

import com.salty.protos.ObtainSMSCodeReq;

import me.zhixingye.im.listener.RequestCallback;

/**
 * Created by zhixingye on 2020年02月02日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public interface SMSService extends BasicService {
    void obtainVerificationCodeForTelephoneType(String telephone, ObtainSMSCodeReq.CodeType type, RequestCallback<Void> callback);
}
