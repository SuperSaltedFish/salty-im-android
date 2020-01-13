package me.zhixingye.im;

import android.content.Context;

import me.zhixingye.im.service.SMSService;
import me.zhixingye.im.service.UserService;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class IMClient {

    private volatile static IMClient sClient;

    public synchronized static void init(Context context) {
        if (sClient != null) {
            throw new RuntimeException("IMClient 已经初始化");
        }
        if (context == null) {
            throw new RuntimeException("context == null");
        }
        sClient = new IMClient();
    }

    private SMSService mSMSService;
    private UserService mUserService;

    public IMClient() {
        mSMSService = new SMSService();
        mUserService = new UserService();
    }

    public SMSService getSMSService() {
        return mSMSService;
    }

    public UserService getUserService() {
        return mUserService;
    }
}
