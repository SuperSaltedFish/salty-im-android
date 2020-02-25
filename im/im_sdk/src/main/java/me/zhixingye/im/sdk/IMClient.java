package me.zhixingye.im.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import me.zhixingye.im.sdk.proxy.ContactServiceProxy;
import me.zhixingye.im.sdk.proxy.ConversationServiceProxy;
import me.zhixingye.im.sdk.proxy.GroupServiceProxy;
import me.zhixingye.im.sdk.proxy.MessageServiceProxy;
import me.zhixingye.im.sdk.proxy.SMSServiceProxy;
import me.zhixingye.im.sdk.proxy.StorageServiceProxy;
import me.zhixingye.im.sdk.proxy.UserServiceProxy;
import me.zhixingye.im.sdk.util.SystemUtils;
import me.zhixingye.im.service.ContactService;
import me.zhixingye.im.service.ConversationService;
import me.zhixingye.im.service.GroupService;
import me.zhixingye.im.service.MessageService;
import me.zhixingye.im.service.SMSService;
import me.zhixingye.im.service.StorageService;
import me.zhixingye.im.service.UserService;

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

    private ContactServiceProxy mContactService;
    private ConversationServiceProxy mConversationService;
    private GroupServiceProxy mGroupService;
    private MessageServiceProxy mMessageService;
    private SMSServiceProxy mSMSService;
    private StorageServiceProxy mStorageService;
    private UserServiceProxy mUserService;

    private IMClient(Context context, InitCallback callback) {
        mAppContext = context;
        initProxyService();
        initRemoteIMService(callback);
    }

    private void initProxyService() {
        mContactService = new ContactServiceProxy();
        mConversationService = new ConversationServiceProxy();
        mGroupService = new GroupServiceProxy();
        mMessageService = new MessageServiceProxy();
        mSMSService = new SMSServiceProxy();
        mStorageService = new StorageServiceProxy();
        mUserService = new UserServiceProxy();
    }

    private void initRemoteIMService(final InitCallback callback) {
        mAppContext.bindService(new Intent(mAppContext, IMRemoteService.class), new ServiceConnection() {
            private boolean isFirstBind = true;

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                IRemoteService remoteService = IRemoteService.Stub.asInterface(service);
                try {
                    mContactService.bindHandle(remoteService.getContactServiceHandle());
                    mConversationService.bindHandle(remoteService.getConversationServiceHandle());
                    mGroupService.bindHandle(remoteService.getGroupServiceHandle());
                    mMessageService.bindHandle(remoteService.getMessageServiceHandle());
                    mSMSService.bindHandle(remoteService.getSMSServiceHandle());
                    mStorageService.bindHandle(remoteService.getStorageServiceHandle());
                    mUserService.bindHandle(remoteService.getUserServiceHandle());
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

    public SMSService getSMSService() {
        return mSMSService;
    }

    public StorageService getStorageService() {
        return mStorageService;
    }

    public UserService getUserService() {
        return mUserService;
    }

    public interface InitCallback {
        void onCompleted();
    }
}
