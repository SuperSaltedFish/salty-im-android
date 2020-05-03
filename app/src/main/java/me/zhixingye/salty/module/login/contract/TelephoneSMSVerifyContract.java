package me.zhixingye.salty.module.login.contract;


import me.zhixingye.salty.basic.BasicPresenter;
import me.zhixingye.salty.basic.BasicView;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class TelephoneSMSVerifyContract {
    public interface View extends BasicView<Presenter> {
        void showCountDown();

        void startHomeActivity();

        void showRegisterSuccessfulPage();

        void showResetSuccessfulPage();
    }


    public interface Presenter extends BasicPresenter<View> {
        void loginByTelephone(String telephone, String password, String smsCode);

        void registerByTelephone(String telephone, String password, String smsCode);

        void obtainLoginTelephoneSMS(String telephone);

        void obtainRegisterTelephoneSMS(String telephone);
    }
}
