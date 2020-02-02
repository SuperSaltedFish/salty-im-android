package me.zhixingye.im.service;

import com.salty.protos.LoginResp;
import com.salty.protos.RegisterResp;

import androidx.annotation.Nullable;
import me.zhixingye.im.listener.RequestCallback;

/**
 * Created by zhixingye on 2020年02月02日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public interface UserService {
    void registerByTelephone(String telephone, String password, String verificationCode, RequestCallback<RegisterResp> callback);

    void loginByTelephone(String telephone, String password, @Nullable String verificationCode, RequestCallback<LoginResp> callback);
}
