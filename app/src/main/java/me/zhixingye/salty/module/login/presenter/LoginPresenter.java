package me.zhixingye.salty.module.login.presenter;


import com.salty.protos.LoginResp;

import me.zhixingye.im.constant.ResponseCode;
import me.zhixingye.im.sdk.IMClient;
import me.zhixingye.salty.module.login.contract.LoginContract;
import me.zhixingye.salty.widget.listener.LifecycleMVPRequestCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mLoginView;

    @Override
    public void attachView(LoginContract.View view) {
        mLoginView = view;
    }

    @Override
    public void detachView() {
        mLoginView = null;
    }

    @Override
    public void tryLogin(final String username, String password) {
        IMClient.get().getAccountService().loginByTelephone(
                username,
                password,
                null,
                new LifecycleMVPRequestCallback<LoginResp>(mLoginView, false) {
                    @Override
                    protected void onSuccess(LoginResp result) {
                        mLoginView.startHomeActivity();
                    }

                    @Override
                    protected boolean onError(int code, String error) {
                        if (code == ResponseCode.REMOTE_NEED_LOGIN_AUTH.getCode()) {
                            mLoginView.startPhoneVerifyActivity();
                            return true;
                        }
                        return false;
                    }
                });
    }
}
