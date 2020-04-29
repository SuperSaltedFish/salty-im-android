package me.zhixingye.im;

import android.content.Context;

import me.zhixingye.im.service.AccountService;
import me.zhixingye.im.service.ApiService;
import me.zhixingye.im.service.DeviceService;
import me.zhixingye.im.service.SMSService;
import me.zhixingye.im.service.impl.AccountServiceImpl;
import me.zhixingye.im.service.impl.ApiServiceImpl;
import me.zhixingye.im.service.ContactService;
import me.zhixingye.im.service.ConversationService;
import me.zhixingye.im.service.GroupService;
import me.zhixingye.im.service.MessageService;
import me.zhixingye.im.service.StorageService;
import me.zhixingye.im.service.UserService;
import me.zhixingye.im.service.impl.ContactServiceImpl;
import me.zhixingye.im.service.impl.ConversationServiceImpl;
import me.zhixingye.im.service.impl.DeviceServiceImpl;
import me.zhixingye.im.service.impl.GroupServiceImpl;
import me.zhixingye.im.service.impl.MessageServiceImpl;
import me.zhixingye.im.service.impl.SMSServiceImpl;
import me.zhixingye.im.service.impl.ServiceAccessor;
import me.zhixingye.im.service.impl.StorageServiceImpl;
import me.zhixingye.im.service.impl.UserServiceImpl;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class IMCore {

    private static final String TAG = "IMCore";

    private volatile static IMCore sClient;

    public synchronized static void tryInit(Context context) {
        if (sClient != null) {
            throw new RuntimeException("IMCore 已经初始化");
        }
        if (context == null) {
            throw new RuntimeException("context == null");
        }
        sClient = new IMCore(context);
    }

    public static IMCore get() {
        if (sClient == null) {
            throw new RuntimeException("IMCore 未初始化");
        }
        return sClient;
    }

    private Context mAppContext;

    private IMCore(Context context) {
        mAppContext = context.getApplicationContext();

        ServiceAccessor.register(AccountService.class, new AccountServiceImpl());
        ServiceAccessor.register(ApiService.class, new ApiServiceImpl());
        ServiceAccessor.register(ContactService.class, new ContactServiceImpl());
        ServiceAccessor.register(ConversationService.class, new ConversationServiceImpl());
        ServiceAccessor.register(DeviceService.class, new DeviceServiceImpl());
        ServiceAccessor.register(GroupService.class, new GroupServiceImpl());
        ServiceAccessor.register(MessageService.class, new MessageServiceImpl());
        ServiceAccessor.register(SMSService.class, new SMSServiceImpl());
        ServiceAccessor.register(StorageService.class, new StorageServiceImpl());
        ServiceAccessor.register(UserService.class, new UserServiceImpl());

    }

    public AccountService getAccountService() {
        return ServiceAccessor.get(AccountService.class);
    }

    public ContactService getContactService() {
        return ServiceAccessor.get(ContactService.class);
    }

    public ConversationService getConversationService() {
        return ServiceAccessor.get(ConversationService.class);
    }

    public GroupService getGroupService() {
        return ServiceAccessor.get(GroupService.class);
    }

    public MessageService getMessageService() {
        return ServiceAccessor.get(MessageService.class);
    }

    public SMSService getSMSService() {
        return ServiceAccessor.get(SMSService.class);
    }

    public StorageService getStorageService() {
        return ServiceAccessor.get(StorageService.class);
    }

    public UserService getUserService() {
        return ServiceAccessor.get(UserService.class);
    }


    public Context getAppContext() {
        return mAppContext;
    }

}
