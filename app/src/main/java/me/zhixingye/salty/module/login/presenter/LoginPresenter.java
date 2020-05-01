package me.zhixingye.salty.module.login.presenter;


import com.salty.protos.LoginResp;

import me.zhixingye.im.constant.ResponseCode;
import me.zhixingye.im.sdk.IMClient;
import me.zhixingye.salty.module.login.contract.LoginContract;
import me.zhixingye.salty.widget.listener.LifecycleMVPRequestCallback;

/**
 * Created by YZX on 2017年10月20日.
 * 每一个不曾起舞的日子，都是对生命的辜负。
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
                            mLoginView.jumpToVerifyPage();
                            return true;
                        }
                        return false;
                    }
                });
    }
}
