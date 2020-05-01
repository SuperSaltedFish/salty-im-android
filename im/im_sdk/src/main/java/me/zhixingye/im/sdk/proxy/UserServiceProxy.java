package me.zhixingye.im.sdk.proxy;

import com.salty.protos.GetUserInfoResp;
import com.salty.protos.QueryUserInfoResp;
import com.salty.protos.UpdateUserInfoResp;
import com.salty.protos.UserProfile;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.UserService;
import me.zhixingye.im.sdk.IRemoteService;
import me.zhixingye.im.sdk.IUserServiceHandle;
import me.zhixingye.im.sdk.util.CallbackUtil;
import me.zhixingye.im.tool.Logger;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class UserServiceProxy implements UserService, RemoteProxy {

    private static final String TAG = "ContactServiceProxy";

    private IUserServiceHandle mUserHandle;

    @Override
    public void onBindHandle(IRemoteService service) {
        try {
            mUserHandle = service.getUserServiceHandle();
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            mUserHandle = null;
        }
    }

    @Override
    public UserProfile getUserProfile() {
        return null;
    }

    @Override
    public void updateUserInfo(String nickname, String description, UserProfile.Sex sex, long birthday, String location, RequestCallback<UpdateUserInfoResp> callback) {
        try {
            mUserHandle.updateUserInfo(nickname, description, sex.getNumber(), birthday, location, new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void getUserInfoByUserId(String userId, RequestCallback<GetUserInfoResp> callback) {
        try {
            mUserHandle.getUserInfoByUserId(userId, new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void queryUserInfoByTelephone(String telephone, RequestCallback<QueryUserInfoResp> callback) {
        try {
            mUserHandle.queryUserInfoByTelephone(telephone, new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
            CallbackUtil.callRemoteError(callback);
        }
    }

    @Override
    public void queryUserInfoByEmail(String email, RequestCallback<QueryUserInfoResp> callback) {
        try {
            mUserHandle.queryUserInfoByEmail(email, new RemoteCallbackWrapper<>(callback));
        } catch (Exception e) {
            Logger.e(TAG, "远程调用失败", e);
            ProxyHelper.callRemoteFail(callback);
            CallbackUtil.callRemoteError(callback);
        }
    }
}