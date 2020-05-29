package me.zhixingye.salty.module.login.presenter;

import com.salty.protos.LoginResp;
import com.salty.protos.ObtainTelephoneSMSCodeResp;
import com.salty.protos.SMSOperationType;

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
    public void loginByTelephone(String telephone, String password) {
        IMClient.get().getAccountService().loginByTelephone(
                telephone,
                password,
                new LifecycleMVPRequestCallback<LoginResp>(mView) {
                    @Override
                    protected void onSuccess(LoginResp result) {
                        mView.startHomeActivity();
                    }
                });
    }

    @Override
    public void obtainLoginTelephoneSMS(String telephone) {
        obtainTelephoneSMS(telephone, SMSOperationType.LOGIN);
    }

    private void obtainTelephoneSMS(String telephone, SMSOperationType type) {
        IMClient.get().getSMSService().obtainVerificationCodeForTelephoneType(
                telephone,
                type,
                new LifecycleMVPRequestCallback<ObtainTelephoneSMSCodeResp>(mView) {
                    @Override
                    protected void onSuccess(ObtainTelephoneSMSCodeResp result) {
                        mView.showCountDown();
                    }
                });
    }
}
