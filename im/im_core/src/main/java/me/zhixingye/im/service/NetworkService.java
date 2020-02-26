package me.zhixingye.im.service;

import android.content.Context;

import com.google.protobuf.Any;
import com.google.protobuf.MessageLite;
import com.salty.protos.GrpcReq;

import java.util.Locale;

import io.grpc.ManagedChannel;
import io.grpc.android.AndroidChannelBuilder;
import io.grpc.okhttp.OkHttpChannelBuilder;

/**
 * Created by zhixingye on 2020年01月10日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class NetworkService {

    private static NetworkService sService;
    private static Adapter sAdapter;

    public synchronized static void init(Context context, String ip, int port, Adapter adapter) {
        if (sService != null) {
            throw new RuntimeException("NetworkService 已经初始化");
        }
        if (adapter == null) {
            throw new RuntimeException("adapter == null");
        }
        sAdapter = adapter;
        sService = new NetworkService(context, ip, port);
    }

    public synchronized static void destroy() {
        if (sService != null) {
            sService.mChannel.shutdownNow();
            sService = null;
        }
    }

    public static NetworkService get() {
        if (sService == null) {
            throw new RuntimeException("NetworkService 未初始化");
        }
        return sService;
    }

    static GrpcReq createReq(MessageLite message) {
        Any data = Any.newBuilder()
                .setTypeUrl("salty/" + message.getClass().getCanonicalName())
                .setValue(message.toByteString())
                .build();

        return GrpcReq.newBuilder()
                .setDeviceId(sAdapter.getDeviceId())
                .setOs(GrpcReq.OS.ANDROID)
                .setLanguage(GrpcReq.Language.CHINESE)
                .setVersion(sAdapter.getAppVersion())
                .setToken(sAdapter.getDeviceId())
                .setData(data)
                .build();
    }

    public interface Adapter {
        String getDeviceId();

        String getToken();

        String getAppVersion();

        Locale getLanguage();
    }


    private ManagedChannel mChannel;

    private NetworkService(Context context, String ip, int port) {
        OkHttpChannelBuilder builder = OkHttpChannelBuilder.forAddress(ip, port)
                .usePlaintext();

        if (context == null) {
            mChannel = builder.build();
        } else {
            mChannel = AndroidChannelBuilder.usingBuilder(builder)
                    .context(context.getApplicationContext())
                    .build();
        }
    }

    public ManagedChannel getChannel() {
        return mChannel;
    }
}
