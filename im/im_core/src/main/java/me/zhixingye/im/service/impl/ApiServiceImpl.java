package me.zhixingye.im.service.impl;

import android.content.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

import io.grpc.ManagedChannel;
import io.grpc.android.AndroidChannelBuilder;
import io.grpc.okhttp.OkHttpChannelBuilder;
import me.zhixingye.im.api.BasicApi;
import me.zhixingye.im.api.ContactApi;
import me.zhixingye.im.api.ConversationApi;
import me.zhixingye.im.api.GroupApi;
import me.zhixingye.im.api.MessageApi;
import me.zhixingye.im.api.SMSApi;
import me.zhixingye.im.api.UserApi;
import me.zhixingye.im.service.ApiService;

/**
 * Created by zhixingye on 2020年01月10日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class ApiServiceImpl implements ApiService {

    private ManagedChannel mChannel;
    private Adapter mAdapter;

    public ApiServiceImpl(Context context, String serverAddress, Adapter adapter) {
        if (adapter == null) {
            throw new RuntimeException("adapter == null");
        }
        mAdapter = adapter;
        initChannel(context, serverAddress);
    }

    private void initChannel(Context context, String serverAddress) {
        OkHttpChannelBuilder builder = OkHttpChannelBuilder.forTarget(serverAddress)
                .usePlaintext();
        if (context == null) {
            mChannel = builder.build();
        } else {
            mChannel = AndroidChannelBuilder.usingBuilder(builder)
                    .context(context.getApplicationContext())
                    .build();
        }
    }


    public synchronized void release() {
        if (mChannel != null) {
            mChannel.shutdownNow();
            mChannel = null;
        }
    }

    @Override
    public <T extends BasicApi> T createApi(Class<T> c) {
        if (mChannel == null) {
            return null;
        }
        try {
            Constructor<T> constructor = c.getConstructor(ManagedChannel.class, Adapter.class);
            return constructor.newInstance(mChannel, mAdapter);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface Adapter {
        String getDeviceId();

        String getToken();

        String getAppVersion();

        Locale getLanguage();
    }
}
