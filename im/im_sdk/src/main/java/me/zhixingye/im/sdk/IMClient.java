package me.zhixingye.im.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;

import me.zhixingye.im.manager.ContactManager;
import me.zhixingye.im.manager.ConversationManager;
import me.zhixingye.im.manager.GroupManager;
import me.zhixingye.im.manager.MessageManager;
import me.zhixingye.im.manager.StorageManager;
import me.zhixingye.im.manager.UserManager;
import me.zhixingye.im.sdk.proxy.BasicProxy;
import me.zhixingye.im.sdk.proxy.ContactManagerProxy;
import me.zhixingye.im.sdk.proxy.ConversationManagerProxy;
import me.zhixingye.im.sdk.proxy.GroupManagerProxy;
import me.zhixingye.im.sdk.proxy.MessageManagerProxy;
import me.zhixingye.im.sdk.proxy.StorageManagerProxy;
import me.zhixingye.im.sdk.proxy.UserManagerProxy;
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

    private ContactManagerProxy mContactService;
    private ConversationManagerProxy mConversationService;
    private GroupManagerProxy mGroupService;
    private MessageManagerProxy mMessageService;
    private StorageManagerProxy mStorageService;
    private UserManagerProxy mUserService;

    private IRemoteService mIRemoteService;

    private IMClient(Context context) {
        mAppContext = context;
        initProxyService();
    }

    private void initProxyService() {
        mContactService = BasicProxy.createProxy(new ContactManagerProxy(mIMServiceConnector));
        mConversationService = BasicProxy.createProxy(new ConversationManagerProxy(mIMServiceConnector));
        mGroupService = BasicProxy.createProxy(new GroupManagerProxy(mIMServiceConnector));
        mMessageService = BasicProxy.createProxy(new MessageManagerProxy(mIMServiceConnector));
        mStorageService = BasicProxy.createProxy(new StorageManagerProxy(mIMServiceConnector));
        mUserService = BasicProxy.createProxy(new UserManagerProxy(mIMServiceConnector));
    }

    private final BasicProxy.IMServiceConnector mIMServiceConnector = callback -> {
        if (mIRemoteService != null) {
            if (callback != null) {
                callback.onCompleted(mIRemoteService);
            }
            return;
        }
        mAppContext.bindService(new Intent(mAppContext, IMRemoteService.class), new ServiceConnection() {
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


    public ContactManager getContactService() {
        return mContactService;
    }

    public ConversationManager getConversationService() {
        return mConversationService;
    }

    public GroupManager getGroupService() {
        return mGroupService;
    }

    public MessageManager getMessageService() {
        return mMessageService;
    }

    public StorageManager getStorageService() {
        return mStorageService;
    }

    public UserManager getUserService() {
        return mUserService;
    }

}
