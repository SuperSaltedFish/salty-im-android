package me.zhixingye.im.api;

import android.content.Context;

import java.util.Locale;

import io.grpc.ManagedChannel;
import io.grpc.android.AndroidChannelBuilder;
import io.grpc.okhttp.OkHttpChannelBuilder;

/**
 * Created by zhixingye on 2020年01月10日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class ApiService {

    private ContactApi mContactApi;
    private ConversationApi mConversationApi;
    private GroupApi mGroupApi;
    private MessageApi mMessageApi;
    private SMSApi mSMSApi;
    private UserApi mUserApi;

    private ManagedChannel mChannel;


    public ApiService(Context context, String ip, int port, Adapter adapter) {
        if (adapter == null) {
            throw new RuntimeException("adapter == null");
        }

        initChannel(context, ip, port);
        initApis(adapter);
    }

    private void initChannel(Context context, String ip, int port) {
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

    private void initApis(Adapter adapter) {
        mContactApi = new ContactApi(mChannel, adapter);
        mConversationApi = new ConversationApi(mChannel, adapter);
        mGroupApi = new GroupApi(mChannel, adapter);
        mMessageApi = new MessageApi(mChannel, adapter);
        mSMSApi = new SMSApi(mChannel, adapter);
        mUserApi = new UserApi(mChannel, adapter);
    }

    public ContactApi getContactApi() {
        return mContactApi;
    }

    public ConversationApi getConversationApi() {
        return mConversationApi;
    }

    public GroupApi getGroupApi() {
        return mGroupApi;
    }

    public MessageApi getMessageApi() {
        return mMessageApi;
    }

    public SMSApi getSMSApi() {
        return mSMSApi;
    }

    public UserApi getUserApi() {
        return mUserApi;
    }

    public synchronized void release() {
        if (mChannel != null) {
            mChannel.shutdownNow();
            mChannel = null;
        }
    }

    public interface Adapter {
        String getDeviceId();

        String getToken();

        String getAppVersion();

        Locale getLanguage();
    }
}
