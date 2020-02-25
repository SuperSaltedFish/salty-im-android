package me.zhixingye.im.sdk.proxy;

import android.os.RemoteException;

import com.salty.protos.AddGroupMemberResp;
import com.salty.protos.CreateGroupResp;
import com.salty.protos.JoinGroupResp;
import com.salty.protos.KickGroupMemberResp;
import com.salty.protos.QuitGroupResp;
import com.salty.protos.UpdateGroupNameResp;
import com.salty.protos.UpdateGroupNoticeResp;
import com.salty.protos.UpdateMemberNicknameResp;

import java.util.List;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.sdk.IGroupServiceHandle;
import me.zhixingye.im.sdk.util.CallbackUtil;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class GroupServiceProxy extends BasicProxy implements me.zhixingye.im.service.GroupService {

    private IGroupServiceHandle mServiceHandle;

    public GroupServiceProxy() {
    }

    public void bindHandle(IGroupServiceHandle handle) {
        mServiceHandle = handle;
    }

    public void unbindHandle() {
        mServiceHandle = null;
    }

    @Override
    public void createGroup(String groupName, List<String> memberUserIdArr, RequestCallback<CreateGroupResp> callback) {
        if (isServiceUnavailable(mServiceHandle, callback)) {
            return;
        }
        try {
            mServiceHandle.createGroup(groupName, memberUserIdArr, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void joinGroup(String groupId, String reason, RequestCallback<JoinGroupResp> callback) {
        try {
            mServiceHandle.joinGroup(groupId, reason, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void quitGroup(String groupId, RequestCallback<QuitGroupResp> callback) {
        if (isServiceUnavailable(mServiceHandle, callback)) {
            return;
        }
        try {
            mServiceHandle.quitGroup(groupId, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void addGroupMember(String groupId, List<String> memberUserIdArr, RequestCallback<AddGroupMemberResp> callback) {
        if (isServiceUnavailable(mServiceHandle, callback)) {
            return;
        }
        try {
            mServiceHandle.addGroupMember(groupId, memberUserIdArr, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void kickGroupMember(String groupId, String memberUserId, RequestCallback<KickGroupMemberResp> callback) {
        if (isServiceUnavailable(mServiceHandle, callback)) {
            return;
        }
        try {
            mServiceHandle.kickGroupMember(groupId, memberUserId, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void updateGroupName(String groupId, String groupName, RequestCallback<UpdateGroupNameResp> callback) {
        if (isServiceUnavailable(mServiceHandle, callback)) {
            return;
        }
        try {
            mServiceHandle.updateGroupName(groupId, groupName, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void updateGroupNotice(String groupId, String notice, RequestCallback<UpdateGroupNoticeResp> callback) {
        if (isServiceUnavailable(mServiceHandle, callback)) {
            return;
        }
        try {
            mServiceHandle.updateGroupNotice(groupId, notice, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void updateMemberNickname(String groupId, String memberNickname, RequestCallback<UpdateMemberNicknameResp> callback) {
        if (isServiceUnavailable(mServiceHandle, callback)) {
            return;
        }
        try {
            mServiceHandle.updateMemberNickname(groupId, memberNickname, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }
}
