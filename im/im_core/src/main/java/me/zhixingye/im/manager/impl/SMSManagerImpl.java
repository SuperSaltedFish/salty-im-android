package me.zhixingye.im.manager.impl;

import com.salty.protos.ObtainSMSCodeReq;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.manager.SMSManager;
import me.zhixingye.im.service.SMSService;

/**
 * Created by zhixingye on 2020年01月10日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class SMSManagerImpl implements SMSManager {

    private SMSService mSMSService;

    public SMSManagerImpl() {
        super();
        mSMSService =SMSService.get();
    }

    @Override
    public void obtainVerificationCodeForTelephoneType(String telephone, ObtainSMSCodeReq.CodeType type, RequestCallback<Void> callback) {
        mSMSService.obtainVerificationCodeForTelephoneType(telephone, type, callback);
    }

    public void destroy() {

    }
}
