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
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
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