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
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
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
