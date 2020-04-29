package me.zhixingye.im.sdk.proxy;

import android.os.RemoteException;

import me.zhixingye.im.sdk.IMessageServiceHandle;
import me.zhixingye.im.service.MessageService;
import me.zhixingye.im.sdk.IRemoteService;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class MessageManagerProxy extends BasicProxy implements MessageService {

    private IMessageServiceHandle mMessageHandle;

    public MessageManagerProxy(IMServiceConnector proxy) {
        super(proxy);
    }

    @Override
    protected void onConnectRemoteService(IRemoteService service) {
        try {
            mMessageHandle = service.getMessageServiceHandle();
        } catch (RemoteException e) {
            e.printStackTrace();
            mMessageHandle = null;
        }
    }


    public void bindHandle(IMessageServiceHandle handle) {
        mMessageHandle = handle;
    }

    public void unbindHandle() {
        mMessageHandle = null;
    }
}
