package me.zhixingye.im.sdk.proxy;

import me.zhixingye.im.sdk.IMessageServiceHandle;
import me.zhixingye.im.service.MessageService;
import me.zhixingye.im.sdk.IRemoteService;
import me.zhixingye.im.tool.Logger;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class MessageServiceProxy extends BasicProxy implements MessageService {

    private static final String TAG = "ContactServiceProxy";

    private IMessageServiceHandle mMessageHandle;

    @Override
    public void onBindHandle(IRemoteService service) {
        try {
            mMessageHandle = service.getMessageServiceHandle();
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            mMessageHandle = null;
        }
    }

}
