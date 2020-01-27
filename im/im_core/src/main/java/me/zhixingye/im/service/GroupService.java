package me.zhixingye.im.service;

import com.salty.protos.AddGroupMemberReq;
import com.salty.protos.AddGroupMemberResp;
import com.salty.protos.CreateGroupReq;
import com.salty.protos.CreateGroupResp;
import com.salty.protos.GroupServiceGrpc;
import com.salty.protos.JoinGroupReq;
import com.salty.protos.JoinGroupResp;
import com.salty.protos.KickGroupMemberReq;
import com.salty.protos.KickGroupMemberResp;
import com.salty.protos.QuitGroupReq;
import com.salty.protos.QuitGroupResp;
import com.salty.protos.UpdateGroupNameReq;
import com.salty.protos.UpdateGroupNameResp;
import com.salty.protos.UpdateGroupNoticeReq;
import com.salty.protos.UpdateGroupNoticeResp;
import com.salty.protos.UpdateMemberNicknameReq;
import com.salty.protos.UpdateMemberNicknameResp;

import java.util.List;

import me.zhixingye.im.listener.RequestCallback;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class GroupService extends BasicService {

    private GroupServiceGrpc.GroupServiceStub mGroupServiceStub;

    public GroupService() {
        super();
        mGroupServiceStub = GroupServiceGrpc.newStub(getChannel());
    }

    public void createGroup(String groupName, List<String> memberUserIdArr, RequestCallback<CreateGroupResp> callback) {
        CreateGroupReq req = CreateGroupReq.newBuilder()
                .setGroupName(groupName)
                .addAllMemberUserIdArr(memberUserIdArr)
                .build();
        mGroupServiceStub.createGroup(createReq(req), new DefaultStreamObserver<>(CreateGroupResp.getDefaultInstance(), callback));
    }

    public void joinGroup(String groupId, String reason, RequestCallback<JoinGroupResp> callback) {
        JoinGroupReq req = JoinGroupReq.newBuilder()
                .setGroupId(groupId)
                .setReason(reason)
                .build();
        mGroupServiceStub.createGroup(createReq(req), new DefaultStreamObserver<>(JoinGroupResp.getDefaultInstance(), callback));
    }

    public void quitGroup(String groupId, RequestCallback<QuitGroupResp> callback) {
        QuitGroupReq req = QuitGroupReq.newBuilder()
                .setGroupId(groupId)
                .build();
        mGroupServiceStub.createGroup(createReq(req), new DefaultStreamObserver<>(QuitGroupResp.getDefaultInstance(), callback));
    }

    public void addGroupMember(String groupId, List<String> memberUserIdArr, RequestCallback<AddGroupMemberResp> callback) {
        AddGroupMemberReq req = AddGroupMemberReq.newBuilder()
                .setGroupId(groupId)
                .addAllMemberUserIdArr(memberUserIdArr)
                .build();
        mGroupServiceStub.createGroup(createReq(req), new DefaultStreamObserver<>(AddGroupMemberResp.getDefaultInstance(), callback));
    }

    public void kickGroupMember(String groupId, String memberUserId, RequestCallback<KickGroupMemberResp> callback) {
        KickGroupMemberReq req = KickGroupMemberReq.newBuilder()
                .setGroupId(groupId)
                .setMemberUserId(memberUserId)
                .build();
        mGroupServiceStub.createGroup(createReq(req), new DefaultStreamObserver<>(KickGroupMemberResp.getDefaultInstance(), callback));
    }

    public void updateGroupName(String groupId, String groupName, RequestCallback<UpdateGroupNameResp> callback) {
        UpdateGroupNameReq req = UpdateGroupNameReq.newBuilder()
                .setGroupId(groupId)
                .setGroupName(groupName)
                .build();
        mGroupServiceStub.createGroup(createReq(req), new DefaultStreamObserver<>(UpdateGroupNameResp.getDefaultInstance(), callback));
    }

    public void updateGroupNotice(String groupId, String notice, RequestCallback<UpdateGroupNoticeResp> callback) {
        UpdateGroupNoticeReq req = UpdateGroupNoticeReq.newBuilder()
                .setGroupId(groupId)
                .setNotice(notice)
                .build();
        mGroupServiceStub.createGroup(createReq(req), new DefaultStreamObserver<>(UpdateGroupNoticeResp.getDefaultInstance(), callback));
    }

    public void updateMemberNickname(String groupId, String memberNickname, RequestCallback<UpdateMemberNicknameResp> callback) {
        UpdateMemberNicknameReq req = UpdateMemberNicknameReq.newBuilder()
                .setGroupId(groupId)
                .setMemberNickname(memberNickname)
                .build();
        mGroupServiceStub.createGroup(createReq(req), new DefaultStreamObserver<>(UpdateMemberNicknameResp.getDefaultInstance(), callback));
    }
}
