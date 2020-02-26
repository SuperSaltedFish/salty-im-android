package me.zhixingye.im.manager;

import com.salty.protos.GetUserInfoResp;
import com.salty.protos.QueryUserInfoResp;
import com.salty.protos.ResetPasswordResp;
import com.salty.protos.UpdateUserInfoResp;
import com.salty.protos.UserProfile;

import me.zhixingye.im.listener.RequestCallback;

/**
 * Created by zhixingye on 2020年02月02日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public interface UserManager {
    void resetLoginPasswordByTelephone(String telephone, String verificationCode, String newPassword, RequestCallback<ResetPasswordResp> callback);

    void resetLoginPassword(String telephone, String oldPassword, String newPassword, RequestCallback<ResetPasswordResp> callback);

    void updateUserInfo(String nickname, String description, UserProfile.Sex sex, long birthday, String location, RequestCallback<UpdateUserInfoResp> callback);

    void getUserInfoByUserId(String userId, RequestCallback<GetUserInfoResp> callback);

    void queryUserInfoByTelephone(String telephone, RequestCallback<QueryUserInfoResp> callback);

    void queryUserInfoByEmail(String email, RequestCallback<QueryUserInfoResp> callback);
}
