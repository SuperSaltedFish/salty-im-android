package me.zhixingye.im.service;

import com.salty.protos.LoginResp;
import com.salty.protos.RegisterResp;

import androidx.annotation.Nullable;
import me.zhixingye.im.listener.RequestCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
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
