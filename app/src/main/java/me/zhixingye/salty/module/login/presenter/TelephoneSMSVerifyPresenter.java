package me.zhixingye.salty.module.login.presenter;

import com.salty.protos.ObtainTelephoneSMSCodeResp;
import com.salty.protos.SMSOperationType;
import com.salty.protos.StatusCode;
import com.salty.protos.VerifyTelephoneSMSCodeResp;

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
    public void obtainTelephoneSMS(String telephone, SMSOperationType type, final boolean firstObtain) {
        IMClient.get().getSMSService().obtainVerificationCodeForTelephoneType(
                telephone,
                type,
                new LifecycleMVPRequestCallback<ObtainTelephoneSMSCodeResp>(mView) {
                    @Override
                    protected void onSuccess(ObtainTelephoneSMSCodeResp result) {
                        mView.showSendSuccessful();
                    }

                    @Override
                    protected boolean onError(int code, String error) {
                        boolean isHandled = dispatchErrorLogic(code);
                        if (isHandled) {
                            return true;
                        }
                        if (firstObtain) {
                            mView.showFirstSendFailure(error);
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
    }

    @Override
    public void verifyTelephoneSMS(String telephone, String smsCode, SMSOperationType type) {
        IMClient.get().getSMSService().verifyTelephoneSMSCode(
                telephone,
                smsCode,
                type,
                new LifecycleMVPRequestCallback<VerifyTelephoneSMSCodeResp>(mView) {
                    @Override
                    protected void onSuccess(VerifyTelephoneSMSCodeResp result) {
                        mView.showVerifySuccessful();
                    }
                });
    }

    private boolean dispatchErrorLogic(int code) {
        StatusCode statusCode = StatusCode.forNumber(code);
        if (statusCode == null) {
            return false;
        }
        switch (statusCode) {
            case STATUS_ACCOUNT_EXISTS:
                mView.showRegisteredHintDialog();
                break;
            case STATUS_ACCOUNT_NOT_EXISTS:
                mView.showUnregisteredHintDialog();
                break;
            default:
                return false;
        }
        return true;
    }
}
