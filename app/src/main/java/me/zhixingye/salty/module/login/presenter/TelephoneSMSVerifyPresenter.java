package me.zhixingye.salty.module.login.presenter;

import com.salty.protos.LoginResp;
import com.salty.protos.ObtainSMSCodeReq;
import com.salty.protos.ObtainSMSCodeResp;
import com.salty.protos.RegisterResp;
import com.salty.protos.ResetPasswordResp;

import me.zhixingye.im.sdk.IMClient;
import me.zhixingye.salty.module.login.contract.TelephoneSMSVerifyContract;
import me.zhixingye.salty.widget.listener.LifecycleMVPRequestCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月03日.
 */
public class TelephoneSMSVerifyPresenter implements TelephoneSMSVerifyContract.Presenter {

    private TelephoneSMSVerifyContract.View mView;

    @Override
    public void attachView(TelephoneSMSVerifyContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void loginByTelephone(String telephone, String password, String smsCode) {
        IMClient.get().getAccountService().loginByTelephone(
                telephone,
                password,
                smsCode,
                new LifecycleMVPRequestCallback<LoginResp>(mView) {
                    @Override
                    protected void onSuccess(LoginResp result) {
                        mView.startHomeActivity();
                    }
                });
    }

    @Override
    public void obtainLoginTelephoneSMS(String telephone) {
        obtainTelephoneSMS(telephone, ObtainSMSCodeReq.CodeType.LOGIN);
    }

    private void obtainTelephoneSMS(String telephone, ObtainSMSCodeReq.CodeType type) {
        IMClient.get().getSMSService().obtainVerificationCodeForTelephoneType(
                telephone,
                type,
                new LifecycleMVPRequestCallback<ObtainSMSCodeResp>(mView) {
                    @Override
                    protected void onSuccess(ObtainSMSCodeResp result) {
                        mView.showCountDown();
                    }
                });
    }
}
