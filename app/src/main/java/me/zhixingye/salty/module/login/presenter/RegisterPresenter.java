package me.zhixingye.salty.module.login.presenter;

import com.salty.protos.ObtainSMSCodeReq;
import com.salty.protos.ObtainSMSCodeResp;

import me.zhixingye.im.sdk.IMClient;
import me.zhixingye.salty.module.login.contract.RegisterContract;
import me.zhixingye.salty.widget.listener.LifecycleMVPRequestCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class RegisterPresenter implements RegisterContract.Presenter {

    private RegisterContract.View mRegisterView;

    @Override
    public void attachView(RegisterContract.View view) {
        mRegisterView = view;
    }

    @Override
    public void detachView() {
        mRegisterView = null;
    }

    @Override
    public void obtainTelephoneRegisterVerifyCode(String telephone) {
        IMClient.get().getSMSService().obtainVerificationCodeForTelephoneType(
                telephone,
                ObtainSMSCodeReq.CodeType.REGISTER,
                new LifecycleMVPRequestCallback<ObtainSMSCodeResp>(mRegisterView, false) {
                    @Override
                    protected void onSuccess(ObtainSMSCodeResp result) {

                    }
                });
    }
}
