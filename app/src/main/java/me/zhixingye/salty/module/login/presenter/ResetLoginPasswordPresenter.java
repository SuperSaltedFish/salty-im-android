package me.zhixingye.salty.module.login.presenter;

import com.salty.protos.ObtainTelephoneSMSCodeResp;
import com.salty.protos.RegisterResp;
import com.salty.protos.ResetPasswordResp;
import com.salty.protos.SMSOperationType;

import me.zhixingye.im.sdk.IMClient;
import me.zhixingye.salty.module.login.contract.ResetLoginPasswordContract;
import me.zhixingye.salty.widget.listener.LifecycleMVPRequestCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月03日.
 */
public class ResetLoginPasswordPresenter implements ResetLoginPasswordContract.Presenter {

    private ResetLoginPasswordContract.View mView;

    @Override
    public void attachView(ResetLoginPasswordContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void obtainTelephoneRegisterSMS(String telephone) {
        obtainTelephoneSMS(telephone, SMSOperationType.REGISTER);
    }

    @Override
    public void obtainResetTelephoneLoginPasswordSMS(String telephone) {
        obtainTelephoneSMS(telephone, SMSOperationType.RESET_PASSWORD);
    }

    private void obtainTelephoneSMS(String telephone, SMSOperationType type) {
        IMClient.get().getSMSService().obtainVerificationCodeForTelephoneType(
                telephone,
                type,
                new LifecycleMVPRequestCallback<ObtainTelephoneSMSCodeResp>(mView, false) {
                    @Override
                    protected void onSuccess(ObtainTelephoneSMSCodeResp result) {
                        mView.starResendCountDown();
                    }
                });
    }

    @Override
    public void registerByTelephone(String telephone, String password) {
        IMClient.get().getAccountService().registerByTelephone(
                telephone,
                password,
                new LifecycleMVPRequestCallback<RegisterResp>(mView, false) {
                    @Override
                    protected void onSuccess(RegisterResp result) {
                        mView.showRegisterSuccessfulPage();
                    }
                });
    }

    @Override
    public void resetTelephoneLoginPasswordBySMS(String telephone, String newPassword) {
        IMClient.get().getAccountService().resetLoginPasswordByTelephoneSMS(
                telephone,
                newPassword,
                new LifecycleMVPRequestCallback<ResetPasswordResp>(mView, false) {
                    @Override
                    protected void onSuccess(ResetPasswordResp result) {
                        mView.showResetSuccessfulPage();
                    }
                });
    }

    @Override
    public void resetTelephoneLoginPasswordByOldPassword(String telephone, String oldPassword,
                                                         String newPassword) {
        IMClient.get().getAccountService().resetLoginPasswordByTelephoneOldPassword(
                telephone,
                oldPassword,
                newPassword,
                new LifecycleMVPRequestCallback<ResetPasswordResp>(mView, false) {
                    @Override
                    protected void onSuccess(ResetPasswordResp result) {
                        mView.showResetSuccessfulPage();
                    }
                });
    }
}
