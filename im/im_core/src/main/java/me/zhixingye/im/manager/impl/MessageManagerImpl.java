package me.zhixingye.im.manager.impl;

import me.zhixingye.im.manager.MessageManager;
import me.zhixingye.im.api.MessageApi;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class MessageManagerImpl implements MessageManager {

    private MessageApi mMessageApi;

    public MessageManagerImpl(String userId, MessageApi messageApi) {
        super();
        mMessageApi = messageApi;
    }

}
