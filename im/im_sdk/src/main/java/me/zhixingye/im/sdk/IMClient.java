package me.zhixingye.im.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;

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
import me.zhixingye.im.sdk.proxy.MessageServiceProxy;
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
    private MessageServiceProxy mMessageServiceProxy;
    private SMSServiceProxy mSMSServiceProxy;
    private StorageServiceProxy mStorageServiceProxy;
    private UserServiceProxy mUserServiceProxy;

    private IRemoteService mIRemoteService;

    private IMClient(Context context) {
        mAppContext = context;
        initProxyService();
        autoBindRemoteService();
    }

    private void initProxyService() {
        mAccountServiceProxy = new AccountServiceProxy();
        mContactServiceProxy = new ContactServiceProxy();
        mConversationServiceProxy = new ConversationServiceProxy();
        mGroupServiceProxy = new GroupServiceProxy();
        mMessageServiceProxy = new MessageServiceProxy();
        mSMSServiceProxy = new SMSServiceProxy();
        mStorageServiceProxy = new StorageServiceProxy();
        mUserServiceProxy = new UserServiceProxy();
    }

    private void autoBindRemoteService() {
        mAppContext.bindService(
                new Intent(mAppContext, IMRemoteService.class),
                new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        mIRemoteService = IRemoteService.Stub.asInterface(service);

                        mAccountServiceProxy.onBindHandle(mIRemoteService);
                        mContactServiceProxy.onBindHandle(mIRemoteService);
                        mConversationServiceProxy.onBindHandle(mIRemoteService);
                        mGroupServiceProxy.onBindHandle(mIRemoteService);
                        mMessageServiceProxy.onBindHandle(mIRemoteService);
                        mSMSServiceProxy.onBindHandle(mIRemoteService);
                        mStorageServiceProxy.onBindHandle(mIRemoteService);
                        mUserServiceProxy.onBindHandle(mIRemoteService);
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
    }

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
        return mMessageServiceProxy;
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
