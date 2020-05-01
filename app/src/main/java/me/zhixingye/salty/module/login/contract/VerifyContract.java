package me.zhixingye.salty.module.login.contract;


import me.zhixingye.salty.basic.BasicPresenter;
import me.zhixingye.salty.basic.BasicView;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class VerifyContract {
    public interface View extends BasicView<Presenter> {

        void showCountDown();

        void startHomeActivity();

        void jumpToLoginPage();
    }


    public interface Presenter extends BasicPresenter<View> {

        void login(String username, String password, String verifyCode);

        void register(String username, String password, String nickname, String verifyCode);

        void obtainLoginSMS(String username, String password);

        void obtainRegisterSMS(String username);

    }
}
