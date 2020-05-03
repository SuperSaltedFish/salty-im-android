package me.zhixingye.salty.module.login.presenter;

import com.salty.protos.ObtainSMSCodeReq;
import com.salty.protos.ObtainSMSCodeResp;
import com.salty.protos.RegisterResp;
import com.salty.protos.ResetPasswordResp;

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
        obtainTelephoneSMS(telephone, ObtainSMSCodeReq.CodeType.REGISTER);
    }

    @Override
    public void obtainResetTelephoneLoginPasswordSMS(String telephone) {
        obtainTelephoneSMS(telephone, ObtainSMSCodeReq.CodeType.RESET_PASSWORD);
    }

    private void obtainTelephoneSMS(String telephone, ObtainSMSCodeReq.CodeType type) {
        IMClient.get().getSMSService().obtainVerificationCodeForTelephoneType(
                telephone,
                type,
                new LifecycleMVPRequestCallback<ObtainSMSCodeResp>(mView,false) {
                    @Override
                    protected void onSuccess(ObtainSMSCodeResp result) {
                        mView.starResendCountDown();
                    }

                    @Override
                    protected boolean onError(int code, String error) {
                        mView.showObtainSMSError(error);
                        return true;
                    }
                });
    }

    @Override
    public void registerByTelephone(String telephone, String password, String smsCode) {
        IMClient.get().getAccountService().registerByTelephone(
                telephone,
                password,
                smsCode,
                new LifecycleMVPRequestCallback<RegisterResp>(mView,false) {
                    @Override
                    protected void onSuccess(RegisterResp result) {
                        mView.showRegisterSuccessfulPage();
                    }

                    @Override
                    protected boolean onError(int code, String error) {
                        mView.showConfirmError(error);
                        return true;
                    }
                });
    }

    @Override
    public void resetTelephoneLoginPasswordBySMS(String telephone, String smsCode, String newPassword) {
        IMClient.get().getAccountService().resetLoginPasswordByTelephoneSMS(
                telephone,
                smsCode,
                newPassword,
                new LifecycleMVPRequestCallback<ResetPasswordResp>(mView, false) {
                    @Override
                    protected void onSuccess(ResetPasswordResp result) {
                        mView.showResetSuccessfulPage();
                    }

                    @Override
                    protected boolean onError(int code, String error) {
                        mView.showConfirmError(error);
                        return true;
                    }
                });
    }

    @Override
    public void resetTelephoneLoginPasswordByOldPassword(String telephone, String oldPassword, String newPassword) {
        IMClient.get().getAccountService().resetLoginPasswordByTelephoneOldPassword(
                telephone,
                oldPassword,
                newPassword,
                new LifecycleMVPRequestCallback<ResetPasswordResp>(mView, false) {
                    @Override
                    protected void onSuccess(ResetPasswordResp result) {
                        mView.showResetSuccessfulPage();
                    }

                    @Override
                    protected boolean onError(int code, String error) {
                        mView.showConfirmError(error);
                        return true;
                    }
                });
    }
}
