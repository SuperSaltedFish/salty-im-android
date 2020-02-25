package me.zhixingye.im.service.manager;

import com.salty.protos.ObtainSMSCodeReq;
import com.salty.protos.ObtainSMSCodeResp;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.SMSService;
import me.zhixingye.im.service.impl.SMSServiceImpl;

/**
 * Created by zhixingye on 2020年01月10日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class SMSManager implements SMSService {

    private SMSService mSMSService;

    public SMSManager() {
        super();
        mSMSService = new SMSServiceImpl();
    }

    @Override
    public void obtainVerificationCodeForTelephoneType(String telephone, ObtainSMSCodeReq.CodeType type, RequestCallback<ObtainSMSCodeResp> callback) {
        mSMSService.obtainVerificationCodeForTelephoneType(telephone, type, callback);
    }

    public void destroy() {

    }
}
