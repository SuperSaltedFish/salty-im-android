package me.zhixingye.im.api;

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

import io.grpc.ManagedChannel;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.impl.ApiServiceImpl;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class GroupApi extends BasicApi {

    private GroupServiceGrpc.GroupServiceStub mGroupServiceStub;

    public GroupApi(ManagedChannel channel) {
        mGroupServiceStub = GroupServiceGrpc.newStub(channel);
    }

    public void createGroup(String groupName, List<String> memberUserIdArr, RequestCallback<CreateGroupResp> callback) {
        CreateGroupReq req = CreateGroupReq.newBuilder()
                .setGroupName(groupName)
                .addAllMemberUserIdArr(memberUserIdArr)
                .build();
        mGroupServiceStub.createGroup(
                createReq(req),
                new DefaultStreamObserver<>(CreateGroupResp.getDefaultInstance(), callback));
    }

    public void joinGroup(String groupId, String reason, RequestCallback<JoinGroupResp> callback) {
        JoinGroupReq req = JoinGroupReq.newBuilder()
                .setGroupId(groupId)
                .setReason(reason)
                .build();
        mGroupServiceStub.createGroup(
                createReq(req),
                new DefaultStreamObserver<>(JoinGroupResp.getDefaultInstance(), callback));
    }

    public void quitGroup(String groupId, RequestCallback<QuitGroupResp> callback) {
        QuitGroupReq req = QuitGroupReq.newBuilder()
                .setGroupId(groupId)
                .build();
        mGroupServiceStub.createGroup(
                createReq(req),
                new DefaultStreamObserver<>(QuitGroupResp.getDefaultInstance(), callback));
    }

    public void addGroupMember(String groupId, List<String> memberUserIdArr, RequestCallback<AddGroupMemberResp> callback) {
        AddGroupMemberReq req = AddGroupMemberReq.newBuilder()
                .setGroupId(groupId)
                .addAllMemberUserIdArr(memberUserIdArr)
                .build();
        mGroupServiceStub.createGroup(
                createReq(req),
                new DefaultStreamObserver<>(AddGroupMemberResp.getDefaultInstance(), callback));
    }

    public void kickGroupMember(String groupId, String memberUserId, RequestCallback<KickGroupMemberResp> callback) {
        KickGroupMemberReq req = KickGroupMemberReq.newBuilder()
                .setGroupId(groupId)
                .setMemberUserId(memberUserId)
                .build();
        mGroupServiceStub.createGroup(
                createReq(req),
                new DefaultStreamObserver<>(KickGroupMemberResp.getDefaultInstance(), callback));
    }

    public void updateGroupName(String groupId, String groupName, RequestCallback<UpdateGroupNameResp> callback) {
        UpdateGroupNameReq req = UpdateGroupNameReq.newBuilder()
                .setGroupId(groupId)
                .setGroupName(groupName)
                .build();
        mGroupServiceStub.createGroup(
                createReq(req),
                new DefaultStreamObserver<>(UpdateGroupNameResp.getDefaultInstance(), callback));
    }

    public void updateGroupNotice(String groupId, String notice, RequestCallback<UpdateGroupNoticeResp> callback) {
        UpdateGroupNoticeReq req = UpdateGroupNoticeReq.newBuilder()
                .setGroupId(groupId)
                .setNotice(notice)
                .build();
        mGroupServiceStub.createGroup(
                createReq(req),
                new DefaultStreamObserver<>(UpdateGroupNoticeResp.getDefaultInstance(), callback));
    }

    public void updateMemberNickname(String groupId, String memberNickname, RequestCallback<UpdateMemberNicknameResp> callback) {
        UpdateMemberNicknameReq req = UpdateMemberNicknameReq.newBuilder()
                .setGroupId(groupId)
                .setMemberNickname(memberNickname)
                .build();
        mGroupServiceStub.createGroup(
                createReq(req),
                new DefaultStreamObserver<>(UpdateMemberNicknameResp.getDefaultInstance(), callback));
    }
}
