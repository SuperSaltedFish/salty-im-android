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
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
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
