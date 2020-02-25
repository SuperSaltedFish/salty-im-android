package me.zhixingye.im.service.manager;

import com.salty.protos.GetUserInfoResp;
import com.salty.protos.LoginResp;
import com.salty.protos.QueryUserInfoResp;
import com.salty.protos.RegisterResp;
import com.salty.protos.ResetPasswordResp;
import com.salty.protos.UpdateUserInfoResp;
import com.salty.protos.UserProfile;

import androidx.annotation.Nullable;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.UserService;
import me.zhixingye.im.service.impl.UserServiceImpl;

/**
 * Created by zhixingye on 2019年12月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class UserManager implements UserService {

    private UserService mUserService;

    public UserManager() {
        super();
        mUserService = new UserServiceImpl();
    }

    @Override
    public void registerByTelephone(String telephone, String password, String verificationCode, RequestCallback<RegisterResp> callback) {
        mUserService.registerByTelephone(telephone, password, verificationCode, callback);
    }

    @Override
    public void loginByTelephone(String telephone, String password, @Nullable String verificationCode, RequestCallback<LoginResp> callback) {
        mUserService.loginByTelephone(telephone, password, verificationCode, new RequestCallback<LoginResp>() {
            @Override
            public void onCompleted(LoginResp response) {

            }

            @Override
            public void onFailure(int code, String error) {

            }
        });
    }

    @Override
    public void resetLoginPasswordByTelephone(String telephone, String verificationCode, String newPassword, RequestCallback<ResetPasswordResp> callback) {
        mUserService.resetLoginPasswordByTelephone(telephone, verificationCode, newPassword, callback);
    }

    @Override
    public void resetLoginPassword(String telephone, String oldPassword, String newPassword, RequestCallback<ResetPasswordResp> callback) {
        mUserService.resetLoginPassword(telephone, oldPassword, newPassword, callback);
    }

    @Override
    public void updateUserInfo(String nickname, String description, UserProfile.Sex sex, long birthday, String location, RequestCallback<UpdateUserInfoResp> callback) {
        mUserService.updateUserInfo(nickname, description, sex, birthday, location, callback);
    }

    @Override
    public void getUserInfoByUserId(String userId, RequestCallback<GetUserInfoResp> callback) {
        mUserService.getUserInfoByUserId(userId, callback);
    }

    @Override
    public void queryUserInfoByTelephone(String telephone, RequestCallback<QueryUserInfoResp> callback) {
        mUserService.queryUserInfoByTelephone(telephone, callback);
    }

    @Override
    public void queryUserInfoByEmail(String email, RequestCallback<QueryUserInfoResp> callback) {
        mUserService.queryUserInfoByEmail(email, callback);
    }

    public void destroy() {

    }
}