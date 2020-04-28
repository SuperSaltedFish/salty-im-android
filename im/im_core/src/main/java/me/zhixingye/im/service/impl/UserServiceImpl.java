package me.zhixingye.im.service.impl;

import com.salty.protos.GetUserInfoResp;
import com.salty.protos.QueryUserInfoResp;
import com.salty.protos.UpdateUserInfoResp;
import com.salty.protos.UserProfile;

import me.zhixingye.im.api.UserApi;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.ApiService;
import me.zhixingye.im.service.UserService;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class UserServiceImpl implements UserService {


    private UserProfile mUserProfile;

    public UserServiceImpl() {
    }

    @Override
    public UserProfile getUserProfile() {
        return mUserProfile;
    }

    @Override
    public void updateUserInfo(String nickname, String description, UserProfile.Sex sex, long birthday, String location, RequestCallback<UpdateUserInfoResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(UserApi.class)
                .updateUserInfo(nickname, description, sex, birthday, location, callback);
    }

    @Override
    public void getUserInfoByUserId(String userId, RequestCallback<GetUserInfoResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(UserApi.class)
                .getUserInfoByUserId(userId, callback);
    }

    @Override
    public void queryUserInfoByTelephone(String telephone, RequestCallback<QueryUserInfoResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(UserApi.class)
                .queryUserInfoByTelephone(telephone, callback);
    }

    @Override
    public void queryUserInfoByEmail(String email, RequestCallback<QueryUserInfoResp> callback) {
        ServiceAccessor.get(ApiService.class)
                .createApi(UserApi.class)
                .queryUserInfoByEmail(email, callback);
    }

}