package me.zhixingye.im.sdk.proxy;

import android.os.RemoteException;

import me.zhixingye.im.manager.MessageManager;
import me.zhixingye.im.sdk.IMessageManagerHandle;
import me.zhixingye.im.sdk.IRemoteService;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class MessageManagerProxy extends BasicProxy implements MessageManager {

    private IMessageManagerHandle mMessageHandle;

    public MessageManagerProxy(IMServiceConnector proxy) {
        super(proxy);
    }

    @Override
    protected void onConnectRemoteService(IRemoteService service) {
        try {
            mMessageHandle = service.getMessageManagerHandle();
        } catch (RemoteException e) {
            e.printStackTrace();
            mMessageHandle = null;
        }
    }


    public void bindHandle(IMessageManagerHandle handle) {
        mMessageHandle = handle;
    }

    public void unbindHandle() {
        mMessageHandle = null;
    }
}
