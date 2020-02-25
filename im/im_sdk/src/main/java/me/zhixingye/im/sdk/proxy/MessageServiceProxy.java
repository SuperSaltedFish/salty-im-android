package me.zhixingye.im.sdk.proxy;

import me.zhixingye.im.sdk.IMessageServiceHandle;
import me.zhixingye.im.service.MessageService;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class MessageServiceProxy extends BasicProxy implements MessageService {

    private IMessageServiceHandle mServiceHandle;


    public void bindHandle(IMessageServiceHandle handle) {
        mServiceHandle = handle;
    }

    public void unbindHandle() {
        mServiceHandle = null;
    }
}
