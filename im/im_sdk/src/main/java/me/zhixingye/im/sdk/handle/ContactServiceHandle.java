package me.zhixingye.im.sdk.handle;

import android.os.RemoteException;

import com.salty.protos.AcceptContactResp;
import com.salty.protos.DeleteContactResp;
import com.salty.protos.RefusedContactResp;
import com.salty.protos.RequestContactResp;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.sdk.IContactServiceHandle;
import me.zhixingye.im.sdk.IRemoteCallback;

/**
 * Created by zhixingye on 2020年02月02日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class ContactServiceHandle extends IContactServiceHandle.Stub {
    @Override
    public void requestContact(String userId, String reason, IRemoteCallback callback) {
        IMCore.get().getContactService()
                .requestContact(
                        userId,
                        reason,
                        new ByteRemoteCallback<RequestContactResp>(callback));
    }

    @Override
    public void refusedContact(String userId, String reason, IRemoteCallback callback) {
        IMCore.get().getContactService()
                .refusedContact(
                        userId,
                        reason,
                        new ByteRemoteCallback<RefusedContactResp>(callback));
    }

    @Override
    public void acceptContact(String userId, IRemoteCallback callback) {
        IMCore.get().getContactService()
                .acceptContact(
                        userId,
                        new ByteRemoteCallback<AcceptContactResp>(callback));
    }

    @Override
    public void deleteContact(String userId, IRemoteCallback callback) {
        IMCore.get().getContactService()
                .deleteContact(
                        userId,
                        new ByteRemoteCallback<DeleteContactResp>(callback));
    }
}
