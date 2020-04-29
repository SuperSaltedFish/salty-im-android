package me.zhixingye.im.service.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import io.grpc.ManagedChannel;
import io.grpc.android.AndroidChannelBuilder;
import io.grpc.okhttp.OkHttpChannelBuilder;
import me.zhixingye.im.BuildConfig;
import me.zhixingye.im.IMCore;
import me.zhixingye.im.api.BasicApi;
import me.zhixingye.im.service.ApiService;

/**
 * Created by zhixingye on 2020年01月10日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class ApiServiceImpl implements ApiService {

    private ManagedChannel mChannel;

    public ApiServiceImpl() {
        OkHttpChannelBuilder builder = OkHttpChannelBuilder.forTarget(BuildConfig.SERVER_ADDRESS)
                .usePlaintext();
        mChannel = AndroidChannelBuilder.usingBuilder(builder)
                .context(IMCore.get().getAppContext().getApplicationContext())
                .build();
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
            Constructor<T> constructor = c.getConstructor(ManagedChannel.class);
            return constructor.newInstance(mChannel);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
