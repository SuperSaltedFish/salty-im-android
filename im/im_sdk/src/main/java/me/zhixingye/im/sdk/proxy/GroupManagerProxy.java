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
import me.zhixingye.im.sdk.IGroupManagerHandle;
import me.zhixingye.im.sdk.util.CallbackUtil;
import me.zhixingye.im.manager.GroupManager;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class GroupManagerProxy extends BasicProxy implements GroupManager {

    private IGroupManagerHandle mManagerHandle;

    public GroupManagerProxy() {
    }

    public void bindHandle(IGroupManagerHandle handle) {
        mManagerHandle = handle;
    }

    public void unbindHandle() {
        mManagerHandle = null;
    }

    @Override
    public void createGroup(String groupName, List<String> memberUserIdArr, RequestCallback<CreateGroupResp> callback) {
        if (isServiceUnavailable(mManagerHandle, callback)) {
            return;
        }
        try {
            mManagerHandle.createGroup(groupName, memberUserIdArr, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void joinGroup(String groupId, String reason, RequestCallback<JoinGroupResp> callback) {
        try {
            mManagerHandle.joinGroup(groupId, reason, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void quitGroup(String groupId, RequestCallback<QuitGroupResp> callback) {
        if (isServiceUnavailable(mManagerHandle, callback)) {
            return;
        }
        try {
            mManagerHandle.quitGroup(groupId, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void addGroupMember(String groupId, List<String> memberUserIdArr, RequestCallback<AddGroupMemberResp> callback) {
        if (isServiceUnavailable(mManagerHandle, callback)) {
            return;
        }
        try {
            mManagerHandle.addGroupMember(groupId, memberUserIdArr, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void kickGroupMember(String groupId, String memberUserId, RequestCallback<KickGroupMemberResp> callback) {
        if (isServiceUnavailable(mManagerHandle, callback)) {
            return;
        }
        try {
            mManagerHandle.kickGroupMember(groupId, memberUserId, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void updateGroupName(String groupId, String groupName, RequestCallback<UpdateGroupNameResp> callback) {
        if (isServiceUnavailable(mManagerHandle, callback)) {
            return;
        }
        try {
            mManagerHandle.updateGroupName(groupId, groupName, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void updateGroupNotice(String groupId, String notice, RequestCallback<UpdateGroupNoticeResp> callback) {
        if (isServiceUnavailable(mManagerHandle, callback)) {
            return;
        }
        try {
            mManagerHandle.updateGroupNotice(groupId, notice, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void updateMemberNickname(String groupId, String memberNickname, RequestCallback<UpdateMemberNicknameResp> callback) {
        if (isServiceUnavailable(mManagerHandle, callback)) {
            return;
        }
        try {
            mManagerHandle.updateMemberNickname(groupId, memberNickname, new ResultCallbackWrapper<>(callback));
        } catch (RemoteException e) {
            e.printStackTrace();
            CallbackUtil.callRemoteError(callback);
        }
    }
}
