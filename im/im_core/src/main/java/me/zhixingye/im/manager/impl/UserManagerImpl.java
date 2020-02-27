package me.zhixingye.im.manager.impl;

import com.salty.protos.GetUserInfoResp;
import com.salty.protos.QueryUserInfoResp;
import com.salty.protos.ResetPasswordResp;
import com.salty.protos.UpdateUserInfoResp;
import com.salty.protos.UserProfile;

import me.zhixingye.im.api.UserApi;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.manager.UserManager;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class UserManagerImpl implements UserManager {

    private UserApi mUserApi;

    private UserProfile mUserProfile;

    public UserManagerImpl(UserProfile profile, UserApi userApi) {
        mUserProfile = UserProfile.newBuilder(profile).build();
        mUserApi  = userApi;
    }

    @Override
    public UserProfile getUserProfile() {
        return mUserProfile;
    }

    @Override
    public void updateUserInfo(String nickname, String description, UserProfile.Sex sex, long birthday, String location, RequestCallback<UpdateUserInfoResp> callback) {
        mUserApi.updateUserInfo(nickname, description, sex, birthday, location, callback);
    }

    @Override
    public void getUserInfoByUserId(String userId, RequestCallback<GetUserInfoResp> callback) {
        mUserApi.getUserInfoByUserId(userId, callback);
    }

    @Override
    public void queryUserInfoByTelephone(String telephone, RequestCallback<QueryUserInfoResp> callback) {
        mUserApi.queryUserInfoByTelephone(telephone, callback);
    }

    @Override
    public void queryUserInfoByEmail(String email, RequestCallback<QueryUserInfoResp> callback) {
        mUserApi.queryUserInfoByEmail(email, callback);
    }

}