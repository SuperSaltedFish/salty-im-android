package me.zhixingye.im.service;

import com.salty.protos.LoginResp;
import com.salty.protos.RegisterResp;

import androidx.annotation.Nullable;
import me.zhixingye.im.listener.RequestCallback;

/**
 * Created by zhixingye on 2020年04月28日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public interface AccountService extends BasicService {
    void registerByTelephone(String telephone, String password, String verificationCode, RequestCallback<RegisterResp> callback);

    void registerByEmail(String email, String password, String verificationCode, RequestCallback<RegisterResp> callback);

    void loginByTelephone(String telephone, String password, @Nullable String verificationCode, RequestCallback<LoginResp> callback);

    void loginByEmail(String email, String password, @Nullable String verificationCode, RequestCallback<LoginResp> callback);

    void logout();

    boolean isLogged();

    String getCurrentUserId();

    String getCurrentUserToken();
}
