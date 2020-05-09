package me.zhixingye.im.service.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.android.AndroidChannelBuilder;
import io.grpc.okhttp.OkHttpChannelBuilder;
import me.zhixingye.im.BuildConfig;
import me.zhixingye.im.IMCore;
import me.zhixingye.im.api.BasicApi;
import me.zhixingye.im.service.ApiService;
import me.zhixingye.im.tool.Logger;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class ApiServiceImpl implements ApiService {

    private static final String TAG = "ApiServiceImpl";

    private ManagedChannel mChannel;

    public ApiServiceImpl() {
        OkHttpChannelBuilder builder = OkHttpChannelBuilder.forTarget(BuildConfig.SERVER_ADDRESS)
                .usePlaintext();
        mChannel = AndroidChannelBuilder.usingBuilder(builder)
                .context(IMCore.getAppContext().getApplicationContext())
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
            T instance = c.newInstance();
            instance.onBindManagedChannel(mChannel);
            return instance;
        } catch (IllegalAccessException | InstantiationException e) {
            Logger.e(TAG,"创建api失败",e);
        }
        return null;
    }

}
