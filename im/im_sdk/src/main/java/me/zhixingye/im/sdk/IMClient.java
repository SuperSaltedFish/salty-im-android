package me.zhixingye.im.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import me.zhixingye.im.sdk.proxy.ContactManagerProxy;
import me.zhixingye.im.sdk.proxy.ConversationManagerProxy;
import me.zhixingye.im.sdk.proxy.GroupManagerProxy;
import me.zhixingye.im.sdk.proxy.MessageManagerProxy;
import me.zhixingye.im.sdk.proxy.SMSManagerProxy;
import me.zhixingye.im.sdk.proxy.StorageManagerProxy;
import me.zhixingye.im.sdk.proxy.UserManagerProxy;
import me.zhixingye.im.sdk.util.SystemUtils;
import me.zhixingye.im.manager.ContactManager;
import me.zhixingye.im.manager.ConversationManager;
import me.zhixingye.im.manager.GroupManager;
import me.zhixingye.im.manager.MessageManager;
import me.zhixingye.im.manager.SMSManager;
import me.zhixingye.im.manager.StorageManager;
import me.zhixingye.im.manager.UserManager;

/**
 * Created by zhixingye on 2020年02月01日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class IMClient {

    private static IMClient sIMClient;

    public synchronized static void init(Context context, InitCallback callback) {
        String currentProcess = SystemUtils.getCurrentProcessName(context);
        String mainProcess = context.getPackageName();
        if (TextUtils.equals(currentProcess, mainProcess)) {
            if (sIMClient != null) {
                throw new RuntimeException("IMClient 已经初始化");
            }
            context = context.getApplicationContext();
            sIMClient = new IMClient(context, callback);
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
    private SMSManagerProxy mSMSService;
    private StorageManagerProxy mStorageService;
    private UserManagerProxy mUserService;

    private IMClient(Context context, InitCallback callback) {
        mAppContext = context;
        initProxyService();
        initRemoteIMService(callback);
    }

    private void initProxyService() {
        mContactService = new ContactManagerProxy();
        mConversationService = new ConversationManagerProxy();
        mGroupService = new GroupManagerProxy();
        mMessageService = new MessageManagerProxy();
        mSMSService = new SMSManagerProxy();
        mStorageService = new StorageManagerProxy();
        mUserService = new UserManagerProxy();
    }

    private void initRemoteIMService(final InitCallback callback) {
        mAppContext.bindService(new Intent(mAppContext, IMRemoteService.class), new ServiceConnection() {
            private boolean isFirstBind = true;

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                IRemoteService remoteService = IRemoteService.Stub.asInterface(service);
                try {
                    mContactService.bindHandle(remoteService.getContactManagerHandle());
                    mConversationService.bindHandle(remoteService.getConversationManagerHandle());
                    mGroupService.bindHandle(remoteService.getGroupManagerHandle());
                    mMessageService.bindHandle(remoteService.getMessageManagerHandle());
                    mSMSService.bindHandle(remoteService.getSMSManagerHandle());
                    mStorageService.bindHandle(remoteService.getStorageManagerHandle());
                    mUserService.bindHandle(remoteService.getUserManagerHandle());
                    if (isFirstBind) {
                        if (callback != null) {
                            callback.onCompleted();
                        }
                        isFirstBind = false;
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                    onServiceDisconnected(name);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mAppContext.unbindService(this);
                mContactService.unbindHandle();
                mConversationService.unbindHandle();
                mGroupService.unbindHandle();
                mMessageService.unbindHandle();
                mSMSService.unbindHandle();
                mStorageService.unbindHandle();
                mUserService.unbindHandle();
            }

            @Override
            public void onBindingDied(ComponentName name) {
                onServiceDisconnected(name);
            }

        }, Context.BIND_AUTO_CREATE);
    }

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

    public SMSManager getSMSService() {
        return mSMSService;
    }

    public StorageManager getStorageService() {
        return mStorageService;
    }

    public UserManager getUserService() {
        return mUserService;
    }

    public interface InitCallback {
        void onCompleted();
    }
}
