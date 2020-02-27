package me.zhixingye.im.api;

import io.grpc.ManagedChannel;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class MessageApi extends BasicApi{

    public MessageApi(ManagedChannel channel, ApiService.Adapter adapter) {
        super(adapter);
    }
}
