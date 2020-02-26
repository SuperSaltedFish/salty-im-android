package me.zhixingye.im.manager;

import com.salty.protos.ObtainSMSCodeReq;
import com.salty.protos.ObtainSMSCodeResp;

import me.zhixingye.im.listener.RequestCallback;

/**
 * Created by zhixingye on 2020年02月02日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public interface SMSManager {
    void obtainVerificationCodeForTelephoneType(String telephone, ObtainSMSCodeReq.CodeType type, RequestCallback<ObtainSMSCodeResp> callback);
}
