package me.zhixingye.im.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;

import me.zhixingye.im.service.ContactService;
import me.zhixingye.im.service.ConversationService;
import me.zhixingye.im.service.GroupService;
import me.zhixingye.im.service.MessageService;
import me.zhixingye.im.service.StorageService;
import me.zhixingye.im.service.UserService;
import me.zhixingye.im.sdk.proxy.BasicProxy;
import me.zhixingye.im.sdk.proxy.ContactServiceProxy;
import me.zhixingye.im.sdk.proxy.ConversationServiceProxy;
import me.zhixingye.im.sdk.proxy.GroupServiceProxy;
import me.zhixingye.im.sdk.proxy.MessageManagerProxy;
import me.zhixingye.im.sdk.proxy.StorageServiceProxy;
import me.zhixingye.im.sdk.proxy.UserServiceProxy;
import me.zhixingye.im.sdk.util.SystemUtils;

/**
 * Created by zhixingye on 2020年02月01日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class IMClient {

    private static IMClient sIMClient;

    public synchronized static void init(Context context) {
        String currentProcess = SystemUtils.getCurrentProcessName(context);
        String mainProcess = context.getPackageName();
        if (TextUtils.equals(currentProcess, mainProcess)) {
            if (sIMClient != null) {
                throw new RuntimeException("IMClient 已经初始化");
            }
            context = context.getApplicationContext();
            sIMClient = new IMClient(context);
        }
    }

    public static IMClient get() {
        if (sIMClient == null) {
            throw new RuntimeException("IMClient 未初始化");
        }
        return sIMClient;
    }

    private Context mAppContext;

    private ContactServiceProxy mContactService;
    private ConversationServiceProxy mConversationService;
    private GroupServiceProxy mGroupService;
    private MessageManagerProxy mMessageService;
    private StorageServiceProxy mStorageService;
    private UserServiceProxy mUserService;

    private IRemoteService mIRemoteService;

    private IMClient(Context context) {
        mAppContext = context;
        initProxyService();
    }

    private void initProxyService() {
        mContactService = BasicProxy.createProxy(new ContactServiceProxy(mIMServiceConnector));
        mConversationService = BasicProxy.createProxy(new ConversationServiceProxy(mIMServiceConnector));
        mGroupService = BasicProxy.createProxy(new GroupServiceProxy(mIMServiceConnector));
        mMessageService = BasicProxy.createProxy(new MessageManagerProxy(mIMServiceConnector));
        mStorageService = BasicProxy.createProxy(new StorageServiceProxy(mIMServiceConnector));
        mUserService = BasicProxy.createProxy(new UserServiceProxy(mIMServiceConnector));
    }

    private final BasicProxy.IMServiceConnector mIMServiceConnector = callback -> {
        if (mIRemoteService != null) {
            if (callback != null) {
                callback.onCompleted(mIRemoteService);
            }
            return;
        }
        Intent intent = new Intent(mAppContext, IMRemoteService.class);
        mAppContext.bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mIRemoteService = IRemoteService.Stub.asInterface(service);
                if (callback != null) {
                    callback.onCompleted(mIRemoteService);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mIRemoteService = null;
            }

            @Override
            public void onBindingDied(ComponentName name) {
                onServiceDisconnected(name);
            }

        }, Context.BIND_AUTO_CREATE);
    };


    public ContactService getContactService() {
        return mContactService;
    }

    public ConversationService getConversationService() {
        return mConversationService;
    }

    public GroupService getGroupService() {
        return mGroupService;
    }

    public MessageService getMessageService() {
        return mMessageService;
    }

    public StorageService getStorageService() {
        return mStorageService;
    }

    public UserService getUserService() {
        return mUserService;
    }

}
