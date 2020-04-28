package me.zhixingye.im.service;

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

/**
 * Created by zhixingye on 2020年02月02日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public interface GroupService extends BasicService {
    void createGroup(String groupName, List<String> memberUserIdArr, RequestCallback<CreateGroupResp> callback);

    void joinGroup(String groupId, String reason, RequestCallback<JoinGroupResp> callback);

    void quitGroup(String groupId, RequestCallback<QuitGroupResp> callback);

    void addGroupMember(String groupId, List<String> memberUserIdArr, RequestCallback<AddGroupMemberResp> callback);

    void kickGroupMember(String groupId, String memberUserId, RequestCallback<KickGroupMemberResp> callback);

    void updateGroupName(String groupId, String groupName, RequestCallback<UpdateGroupNameResp> callback);

    void updateGroupNotice(String groupId, String notice, RequestCallback<UpdateGroupNoticeResp> callback);

    void updateMemberNickname(String groupId, String memberNickname, RequestCallback<UpdateMemberNicknameResp> callback);
}
