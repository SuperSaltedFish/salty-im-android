package me.zhixingye.im.manager;

import io.grpc.ManagedChannel;

/**
 * Created by zhixingye on 2020年02月02日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public interface NetworkManager {
    ManagedChannel getChannel();
}
