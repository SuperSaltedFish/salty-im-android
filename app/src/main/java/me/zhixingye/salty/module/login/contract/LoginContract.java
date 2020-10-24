package me.zhixingye.salty.module.login.contract;


import me.zhixingye.base.component.mvp.IPresenter;
import me.zhixingye.base.component.mvp.IView;
import me.zhixingye.salty.module.login.presenter.LoginPresenter;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */


public class LoginContract {

    public interface View extends IView<LoginPresenter> {
        void startPhoneVerifyActivity();

        void startHomeActivity();
    }


    public interface Presenter extends IPresenter<View> {
        void tryLoginByTelephone(String username, String password);
    }


}
