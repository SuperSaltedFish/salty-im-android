// IGroupServiceHandle.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.IRemoteCallback;

interface IGroupServiceHandle {
    void createGroup(String groupName, out List<String> memberUserIdArr, IRemoteCallback callback);

    void joinGroup(String groupId, String reason, IRemoteCallback callback);

    void quitGroup(String groupId, IRemoteCallback callback);

    void addGroupMember(String groupId, out List<String> memberUserIdArr, IRemoteCallback callback);

    void kickGroupMember(String groupId, String memberUserId, IRemoteCallback callback);

    void updateGroupName(String groupId, String groupName, IRemoteCallback callback);

    void updateGroupNotice(String groupId, String notice, IRemoteCallback callback);

    void updateMemberNickname(String groupId, String memberNickname, IRemoteCallback callback);
}
