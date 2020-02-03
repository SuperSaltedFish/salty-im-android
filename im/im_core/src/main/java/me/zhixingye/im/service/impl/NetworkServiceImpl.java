package me.zhixingye.im.service.impl;

import android.content.Context;

import io.grpc.ManagedChannel;
import io.grpc.android.AndroidChannelBuilder;
import io.grpc.okhttp.OkHttpChannelBuilder;
import me.zhixingye.im.service.NetworkService;

/**
 * Created by zhixingye on 2020年01月10日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class NetworkServiceImpl implements NetworkService {

    private static NetworkServiceImpl sService;

    public synchronized static void init(String ip, int port, Context context) {
        if (sService != null) {
            throw new RuntimeException("NetworkService 已经初始化");
        }
        sService = new NetworkServiceImpl(ip, port, context);
    }

    public synchronized static void destroy() {
        if (sService != null) {
            sService.mChannel.shutdownNow();
            sService = null;
        }
    }

    public static NetworkServiceImpl get() {
        return sService;
    }


    private ManagedChannel mChannel;

    private NetworkServiceImpl(String ip, int port, Context context) {
        OkHttpChannelBuilder builder = OkHttpChannelBuilder.forAddress(ip, port)
                .usePlaintext();

        mChannel = AndroidChannelBuilder.usingBuilder(builder)
                .context(context.getApplicationContext())
                .build();
    }

    @Override
    public ManagedChannel getChannel() {
        return mChannel;
    }
}
