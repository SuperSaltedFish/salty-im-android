package me.zhixingye.salty.module.login.presenter;


import com.salty.protos.LoginResp;
import com.salty.protos.ObtainSMSCodeReq;
import com.salty.protos.ObtainSMSCodeResp;
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

    private LoginContract.View mView;

    @Override
    public void attachView(LoginContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void tryLoginByTelephone(final String telephone, String password) {
        IMClient.get().getAccountService().loginByTelephone(
                telephone,
                password,
                null,
                new LifecycleMVPRequestCallback<LoginResp>(mView, false) {
                    @Override
                    protected void onSuccess(LoginResp result) {
                        mView.startHomeActivity();
                    }

                    @Override
                    protected boolean onError(int code, String error) {
                        if (code == ResponseCode.REMOTE_NEED_LOGIN_AUTH.getCode()) {
                            obtainTelephoneSMS(telephone);
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean isEnableCancelProgressButtonLoadingOnSuccessful() {
                        return false;
                    }
                });
    }

    private void obtainTelephoneSMS(String telephone) {
        IMClient.get().getSMSService().obtainVerificationCodeForTelephoneType(
                telephone,
                ObtainSMSCodeReq.CodeType.LOGIN,
                new LifecycleMVPRequestCallback<ObtainSMSCodeResp>(mView, false) {
                    @Override
                    protected void onSuccess(ObtainSMSCodeResp result) {
                        mView.startPhoneVerifyActivity();
                    }
                });
    }

}
