package me.zhixingye.salty.module.login.presenter;


import com.salty.protos.ObtainTelephoneSMSCodeResp;
import com.salty.protos.SMSOperationType;

import me.zhixingye.im.sdk.IMClient;
import me.zhixingye.salty.module.login.contract.RecoverPasswordContract;
import me.zhixingye.salty.widget.listener.LifecycleMVPRequestCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月03日.
 */
public class RecoverPasswordPresenter implements RecoverPasswordContract.Presenter {

    private RecoverPasswordContract.View mView;

    @Override
    public void attachView(RecoverPasswordContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void obtainResetTelephoneLoginPasswordSMS(String telephone) {
        IMClient.get().getSMSService().obtainVerificationCodeForTelephoneType(
                telephone,
                SMSOperationType.RESET_PASSWORD,
                new LifecycleMVPRequestCallback<ObtainTelephoneSMSCodeResp>(mView, false) {
                    @Override
                    protected void onSuccess(ObtainTelephoneSMSCodeResp result) {
                        mView.gotoResetTelephoneLoginPage();
                    }
                });
    }
}
