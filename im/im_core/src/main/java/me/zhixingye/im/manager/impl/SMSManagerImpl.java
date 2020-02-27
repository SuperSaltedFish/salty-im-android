package me.zhixingye.im.manager.impl;

import com.salty.protos.ObtainSMSCodeReq;

import me.zhixingye.im.api.SMSApi;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.manager.SMSManager;

/**
 * Created by zhixingye on 2020年01月10日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class SMSManagerImpl implements SMSManager {

    private SMSApi mSMSApi;

    public SMSManagerImpl() {
        super();
    }

    @Override
    public void obtainVerificationCodeForTelephoneType(String telephone, ObtainSMSCodeReq.CodeType type, RequestCallback<Void> callback) {
        mSMSApi.obtainVerificationCodeForTelephoneType(telephone, type, callback);
    }

    public void destroy() {

    }
}
