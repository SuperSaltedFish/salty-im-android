package me.zhixingye.im.sdk.handle;

import com.salty.protos.AddGroupMemberResp;
import com.salty.protos.CreateGroupResp;
import com.salty.protos.JoinGroupResp;
import com.salty.protos.KickGroupMemberResp;
import com.salty.protos.QuitGroupResp;
import com.salty.protos.UpdateGroupNameResp;

import java.util.List;

import me.zhixingye.im.IMCore;
import me.zhixingye.im.sdk.IGroupServiceHandle;
import me.zhixingye.im.sdk.IRemoteCallback;

/**
 * Created by zhixingye on 2020年02月02日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class GroupServiceHandle extends IGroupServiceHandle.Stub {
    @Override
    public void createGroup(String groupName, List<String> memberUserIdArr, IRemoteCallback callback) {
        IMCore.get().getGroupService()
                .createGroup(
                        groupName,
                        memberUserIdArr,
                        new ByteRemoteCallback<CreateGroupResp>(callback));
    }

    @Override
    public void joinGroup(String groupId, String reason, IRemoteCallback callback) {
        IMCore.get().getGroupService()
                .joinGroup(
                        groupId,
                        reason, new ByteRemoteCallback<JoinGroupResp>(callback));
    }

    @Override
    public void quitGroup(String groupId, IRemoteCallback callback) {
        IMCore.get().getGroupService()
                .quitGroup(
                        groupId,
                        new ByteRemoteCallback<QuitGroupResp>(callback));
    }

    @Override
    public void addGroupMember(String groupId, List<String> memberUserIdArr, IRemoteCallback callback) {
        IMCore.get().getGroupService()
                .addGroupMember(
                        groupId,
                        memberUserIdArr,
                        new ByteRemoteCallback<AddGroupMemberResp>(callback));
    }

    @Override
    public void kickGroupMember(String groupId, String memberUserId, IRemoteCallback callback) {
        IMCore.get().getGroupService()
                .kickGroupMember(
                        groupId,
                        memberUserId,
                        new ByteRemoteCallback<KickGroupMemberResp>(callback));
    }

    @Override
    public void updateGroupName(String groupId, String groupName, IRemoteCallback callback) {
        IMCore.get().getGroupService()
                .updateGroupName(
                        groupId,
                        groupName,
                        new ByteRemoteCallback<UpdateGroupNameResp>(callback));
    }

    @Override
    public void updateGroupNotice(String groupId, String notice, IRemoteCallback callback) {
        IMCore.get().getGroupService()
                .updateGroupName(
                        groupId,
                        notice,
                        new ByteRemoteCallback<UpdateGroupNameResp>(callback));
    }

    @Override
    public void updateMemberNickname(String groupId, String memberNickname, IRemoteCallback callback) {
        IMCore.get().getGroupService()
                .updateGroupName(
                        groupId,
                        memberNickname,
                        new ByteRemoteCallback<UpdateGroupNameResp>(callback));
    }
}
