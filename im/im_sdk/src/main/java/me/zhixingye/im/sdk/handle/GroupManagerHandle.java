package me.zhixingye.im.sdk.handle;

import android.os.RemoteException;

import java.util.List;

import me.zhixingye.im.sdk.IGroupManagerHandle;
import me.zhixingye.im.sdk.IResultCallback;

/**
 * Created by zhixingye on 2020年02月02日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class GroupManagerHandle extends IGroupManagerHandle.Stub {
    @Override
    public void createGroup(String groupName, List<String> memberUserIdArr, IResultCallback callback) throws RemoteException {

    }

    @Override
    public void joinGroup(String groupId, String reason, IResultCallback callback) throws RemoteException {

    }

    @Override
    public void quitGroup(String groupId, IResultCallback callback) throws RemoteException {

    }

    @Override
    public void addGroupMember(String groupId, List<String> memberUserIdArr, IResultCallback callback) throws RemoteException {

    }

    @Override
    public void kickGroupMember(String groupId, String memberUserId, IResultCallback callback) throws RemoteException {

    }

    @Override
    public void updateGroupName(String groupId, String groupName, IResultCallback callback) throws RemoteException {

    }

    @Override
    public void updateGroupNotice(String groupId, String notice, IResultCallback callback) throws RemoteException {

    }

    @Override
    public void updateMemberNickname(String groupId, String memberNickname, IResultCallback callback) throws RemoteException {

    }
}
