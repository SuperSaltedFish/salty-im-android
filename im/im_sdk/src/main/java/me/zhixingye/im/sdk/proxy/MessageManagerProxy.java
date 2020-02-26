package me.zhixingye.im.sdk.proxy;

import me.zhixingye.im.manager.MessageManager;
import me.zhixingye.im.sdk.IMessageManagerHandle;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class MessageManagerProxy extends BasicProxy implements MessageManager {

    private IMessageManagerHandle mManagerHandle;


    public void bindHandle(IMessageManagerHandle handle) {
        mManagerHandle = handle;
    }

    public void unbindHandle() {
        mManagerHandle = null;
    }
}
