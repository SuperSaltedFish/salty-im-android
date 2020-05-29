package me.zhixingye.salty.module.login.contract;

import me.zhixingye.salty.basic.BasicPresenter;
import me.zhixingye.salty.basic.BasicView;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月03日.
 */
public class ResetLoginPasswordContract {

    public interface View extends BasicView<Presenter> {

        void showRegisterSuccessfulPage();

        void showResetSuccessfulPage();

        void starResendCountDown();
    }


    public interface Presenter extends BasicPresenter<View> {

        void obtainTelephoneRegisterSMS(String telephone);

        void obtainResetTelephoneLoginPasswordSMS(String telephone);

        void registerByTelephone(String telephone, String password);

        void resetTelephoneLoginPasswordBySMS(String telephone, String newPassword);

        void resetTelephoneLoginPasswordByOldPassword(String telephone, String oldPassword, String newPassword);
    }
}
