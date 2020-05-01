package me.zhixingye.im.sdk.proxy;

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
import me.zhixingye.im.sdk.IRemoteService;
import me.zhixingye.im.sdk.util.CallbackUtil;
import me.zhixingye.im.service.GroupService;
import me.zhixingye.im.tool.Logger;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class GroupServiceProxy implements GroupService, RemoteProxy {

    private static final String TAG = "ContactServiceProxy";

    private IGroupServiceHandle mGroupHandle;

    @Override
    public void onBindHandle(IRemoteService service) {
        try {
            mGroupHandle = service.getGroupServiceHandle();
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            mGroupHandle = null;
        }
    }

    @Override
    public void createGroup(String groupName, List<String> memberUserIdArr, RequestCallback<CreateGroupResp> callback) {
        try {
            mGroupHandle.createGroup(groupName, memberUserIdArr, new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void joinGroup(String groupId, String reason, RequestCallback<JoinGroupResp> callback) {
        try {
            mGroupHandle.joinGroup(groupId, reason, new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void quitGroup(String groupId, RequestCallback<QuitGroupResp> callback) {
        try {
            mGroupHandle.quitGroup(groupId, new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void addGroupMember(String groupId, List<String> memberUserIdArr, RequestCallback<AddGroupMemberResp> callback) {
        try {
            mGroupHandle.addGroupMember(groupId, memberUserIdArr, new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void kickGroupMember(String groupId, String memberUserId, RequestCallback<KickGroupMemberResp> callback) {
        try {
            mGroupHandle.kickGroupMember(groupId, memberUserId, new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void updateGroupName(String groupId, String groupName, RequestCallback<UpdateGroupNameResp> callback) {
        try {
            mGroupHandle.updateGroupName(groupId, groupName, new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void updateGroupNotice(String groupId, String notice, RequestCallback<UpdateGroupNoticeResp> callback) {
        try {
            mGroupHandle.updateGroupNotice(groupId, notice, new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void updateMemberNickname(String groupId, String memberNickname, RequestCallback<UpdateMemberNicknameResp> callback) {
        try {
            mGroupHandle.updateMemberNickname(groupId, memberNickname, new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
            CallbackUtil.callRemoteError(callback);
        }
    }
}
