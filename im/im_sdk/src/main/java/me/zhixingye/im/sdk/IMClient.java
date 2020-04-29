package me.zhixingye.im.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;

import java.util.concurrent.Executors;

import me.zhixingye.im.sdk.proxy.AccountServiceProxy;
import me.zhixingye.im.sdk.proxy.SMSServiceProxy;
import me.zhixingye.im.service.AccountService;
import me.zhixingye.im.service.ContactService;
import me.zhixingye.im.service.ConversationService;
import me.zhixingye.im.service.GroupService;
import me.zhixingye.im.service.MessageService;
import me.zhixingye.im.service.SMSService;
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

    private AccountServiceProxy mAccountServiceProxy;
    private ContactServiceProxy mContactServiceProxy;
    private ConversationServiceProxy mConversationServiceProxy;
    private GroupServiceProxy mGroupServiceProxy;
    private MessageManagerProxy mMessageManagerProxy;
    private SMSServiceProxy mSMSServiceProxy;
    private StorageServiceProxy mStorageServiceProxy;
    private UserServiceProxy mUserServiceProxy;

    private IRemoteService mIRemoteService;

    private IMClient(Context context) {
        mAppContext = context;
        initProxyService();
    }

    private void initProxyService() {
        mAccountServiceProxy = BasicProxy.createProxy(new AccountServiceProxy(mIMServiceConnector));
        mContactServiceProxy = BasicProxy.createProxy(new ContactServiceProxy(mIMServiceConnector));
        mConversationServiceProxy = BasicProxy.createProxy(new ConversationServiceProxy(mIMServiceConnector));
        mGroupServiceProxy = BasicProxy.createProxy(new GroupServiceProxy(mIMServiceConnector));
        mMessageManagerProxy = BasicProxy.createProxy(new MessageManagerProxy(mIMServiceConnector));
        mSMSServiceProxy = BasicProxy.createProxy(new SMSServiceProxy(mIMServiceConnector));
        mStorageServiceProxy = BasicProxy.createProxy(new StorageServiceProxy(mIMServiceConnector));
        mUserServiceProxy = BasicProxy.createProxy(new UserServiceProxy(mIMServiceConnector));
    }

    private final BasicProxy.IMServiceConnector mIMServiceConnector = new BasicProxy.IMServiceConnector() {
        @Override
        public void connectRemoteIMService(final BasicProxy.ConnectCallback callback) {
            if (mIRemoteService != null) {
                if (callback != null) {
                    callback.onCompleted(mIRemoteService);
                }
                return;
            }
            mAppContext.bindService(
                    new Intent(mAppContext, IMRemoteService.class),
                    Context.BIND_AUTO_CREATE,
                    Executors.newSingleThreadExecutor(),
                    new ServiceConnection() {
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

                    });
        }
    };

    public AccountService getAccountService() {
        return mAccountServiceProxy;
    }

    public ContactService getContactService() {
        return mContactServiceProxy;
    }

    public ConversationService getConversationService() {
        return mConversationServiceProxy;
    }

    public GroupService getGroupService() {
        return mGroupServiceProxy;
    }

    public MessageService getMessageService() {
        return mMessageManagerProxy;
    }

    public SMSService getSMSService() {
        return mSMSServiceProxy;
    }

    public StorageService getStorageService() {
        return mStorageServiceProxy;
    }

    public UserService getUserService() {
        return mUserServiceProxy;
    }

}
