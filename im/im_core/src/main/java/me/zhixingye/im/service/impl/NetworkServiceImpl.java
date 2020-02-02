package me.zhixingye.im.service.impl;

import io.grpc.ManagedChannel;
import io.grpc.android.AndroidChannelBuilder;
import me.zhixingye.im.service.NetworkService;

/**
 * Created by zhixingye on 2020年01月10日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class NetworkServiceImpl implements NetworkService {

    private static NetworkServiceImpl sService;

    public synchronized static void init(String ip, int port) {
        if (sService != null) {
            throw new RuntimeException("NetworkService 已经初始化");
        }
        sService = new NetworkServiceImpl(ip, port);
    }

    public static NetworkServiceImpl get() {
        return sService;
    }


    private ManagedChannel mChannel;

    public NetworkServiceImpl(String ip, int port) {
        mChannel = AndroidChannelBuilder.forAddress(ip, port)
                .usePlaintext()
                .build();
    }

    @Override
    public ManagedChannel getChannel() {
        return mChannel;
    }
}
