package me.zhixingye.im.service.impl;

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
import me.zhixingye.im.service.ApiService;
import me.zhixingye.im.service.GroupService;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class GroupServiceImpl implements GroupService {


    public GroupServiceImpl() {
    }

    @Override
    public void createGroup(String groupName, List<String> memberUserIdArr, RequestCallback<CreateGroupResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(GroupApi.class)
                .createGroup(groupName, memberUserIdArr, callback);
    }

    @Override
    public void joinGroup(String groupId, String reason, RequestCallback<JoinGroupResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(GroupApi.class)
                .joinGroup(groupId, reason, callback);
    }

    @Override
    public void quitGroup(String groupId, RequestCallback<QuitGroupResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(GroupApi.class)
                .quitGroup(groupId, callback);
    }

    @Override
    public void addGroupMember(String groupId, List<String> memberUserIdArr, RequestCallback<AddGroupMemberResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(GroupApi.class)
                .addGroupMember(groupId, memberUserIdArr, callback);
    }

    @Override
    public void kickGroupMember(String groupId, String memberUserId, RequestCallback<KickGroupMemberResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(GroupApi.class)
                .kickGroupMember(groupId, memberUserId, callback);
    }

    @Override
    public void updateGroupName(String groupId, String groupName, RequestCallback<UpdateGroupNameResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(GroupApi.class)
                .updateGroupName(groupId, groupName, callback);
    }

    @Override
    public void updateGroupNotice(String groupId, String notice, RequestCallback<UpdateGroupNoticeResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(GroupApi.class)
                .updateGroupNotice(groupId, notice, callback);
    }

    @Override
    public void updateMemberNickname(String groupId, String memberNickname, RequestCallback<UpdateMemberNicknameResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(GroupApi.class)
                .updateMemberNickname(groupId, memberNickname, callback);
    }
}