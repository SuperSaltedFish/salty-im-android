package me.zhixingye.im.service;

import com.salty.protos.GetUserInfoResp;
import com.salty.protos.QueryUserInfoResp;
import com.salty.protos.UpdateUserInfoResp;
import com.salty.protos.UserProfile;

import me.zhixingye.im.listener.RequestCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public interface UserService extends BasicService {
    UserProfile getUserProfile();

    void updateUserInfo(String nickname, String description, UserProfile.Sex sex, long birthday, String location, RequestCallback<UpdateUserInfoResp> callback);

    void getUserInfoByUserId(String userId, RequestCallback<GetUserInfoResp> callback);

    void queryUserInfoByTelephone(String telephone, RequestCallback<QueryUserInfoResp> callback);

    void queryUserInfoByEmail(String email, RequestCallback<QueryUserInfoResp> callback);
}
