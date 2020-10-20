package me.zhixingye.salty.module.login.contract;


import com.salty.protos.SMSOperationType;

import me.zhixingye.base.component.mvp.IPresenter;
import me.zhixingye.base.component.mvp.IView;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class TelephoneSMSVerifyContract {
    public interface View extends IView<Presenter> {
        void showSMSInputLayout(int smsCodeLength);

        void showFirstSendFailure(String error);

        void showVerifySuccessful();

        void showRegisteredHintDialog();

        void showUnregisteredHintDialog();
    }


    public interface Presenter extends IPresenter<View> {
        void obtainTelephoneSMS(String telephone, SMSOperationType type, boolean firstObtain);

        void verifyTelephoneSMS(String telephone, String smsCode, SMSOperationType type);
    }
}
