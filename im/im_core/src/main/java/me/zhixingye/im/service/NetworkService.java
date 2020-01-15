package me.zhixingye.im.service;

import com.google.protobuf.MessageLite;

import io.grpc.ManagedChannel;
import io.grpc.android.AndroidChannelBuilder;

/**
 * Created by zhixingye on 2020年01月10日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class NetworkService {

    private static NetworkService sService;

    public synchronized static void init(String ip, int port) {
        if (sService != null) {
            throw new RuntimeException("NetworkService 已经初始化");
        }
        sService = new NetworkService(ip, port);
    }

    public static NetworkService get() {
        return sService;
    }


    private ManagedChannel mChannel;

    public NetworkService(String ip, int port) {
        mChannel = AndroidChannelBuilder.forAddress(ip, port)
                .usePlaintext()
                .build();
    }

    public void sendRequest(MessageLite message) {

    }

    public ManagedChannel getChannel() {
        return mChannel;
    }
}
