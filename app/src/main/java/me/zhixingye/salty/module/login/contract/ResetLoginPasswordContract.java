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
        void showRegisterSuccessful();

        void showResetLoginPasswordSuccessful();
    }


    public interface Presenter extends BasicPresenter<View> {

        void registerByTelephone(String telephone, String password);

        void resetTelephoneLoginPassword(String telephone, String newPassword);
    }
}
