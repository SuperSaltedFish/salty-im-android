// IGroupServiceHandle.aidl
package me.zhixingye.im.sdk;

// Declare any non-default types here with import statements
import me.zhixingye.im.sdk.IResultCallback;

interface IGroupServiceHandle {
    void createGroup(String groupName, out List<String> memberUserIdArr, IResultCallback callback);

    void joinGroup(String groupId, String reason, IResultCallback callback);

    void quitGroup(String groupId, IResultCallback callback);

    void addGroupMember(String groupId, out List<String> memberUserIdArr, IResultCallback callback);

    void kickGroupMember(String groupId, String memberUserId, IResultCallback callback);

    void updateGroupName(String groupId, String groupName, IResultCallback callback);

    void updateGroupNotice(String groupId, String notice, IResultCallback callback);

    void updateMemberNickname(String groupId, String memberNickname, IResultCallback callback);
}
