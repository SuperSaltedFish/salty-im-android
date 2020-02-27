package me.zhixingye.im.manager.impl;

import com.salty.protos.AddGroupMemberResp;
import com.salty.protos.CreateGroupResp;
import com.salty.protos.JoinGroupResp;
import com.salty.protos.KickGroupMemberResp;
import com.salty.protos.QuitGroupResp;
import com.salty.protos.UpdateGroupNameResp;
import com.salty.protos.UpdateGroupNoticeResp;
import com.salty.protos.UpdateMemberNicknameResp;

import java.util.List;

import me.zhixingye.im.api.GroupApi;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.manager.GroupManager;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class GroupManagerImpl implements GroupManager {

    private GroupApi mGroupApi;

    public GroupManagerImpl(String userId, GroupApi groupApi) {
        super();
        mGroupApi =groupApi;
    }

    @Override
    public void createGroup(String groupName, List<String> memberUserIdArr, RequestCallback<CreateGroupResp> callback) {
        mGroupApi.createGroup(groupName, memberUserIdArr, callback);
    }

    @Override
    public void joinGroup(String groupId, String reason, RequestCallback<JoinGroupResp> callback) {
        mGroupApi.joinGroup(groupId, reason, callback);
    }

    @Override
    public void quitGroup(String groupId, RequestCallback<QuitGroupResp> callback) {
        mGroupApi.quitGroup(groupId, callback);
    }

    @Override
    public void addGroupMember(String groupId, List<String> memberUserIdArr, RequestCallback<AddGroupMemberResp> callback) {
        mGroupApi.addGroupMember(groupId, memberUserIdArr, callback);
    }

    @Override
    public void kickGroupMember(String groupId, String memberUserId, RequestCallback<KickGroupMemberResp> callback) {
        mGroupApi.kickGroupMember(groupId, memberUserId, callback);
    }

    @Override
    public void updateGroupName(String groupId, String groupName, RequestCallback<UpdateGroupNameResp> callback) {
        mGroupApi.updateGroupName(groupId, groupName, callback);
    }

    @Override
    public void updateGroupNotice(String groupId, String notice, RequestCallback<UpdateGroupNoticeResp> callback) {
        mGroupApi.updateGroupNotice(groupId, notice, callback);
    }

    @Override
    public void updateMemberNickname(String groupId, String memberNickname, RequestCallback<UpdateMemberNicknameResp> callback) {
        mGroupApi.updateMemberNickname(groupId, memberNickname, callback);
    }
}
