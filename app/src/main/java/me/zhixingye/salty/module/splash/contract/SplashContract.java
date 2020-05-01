package me.zhixingye.salty.module.splash.contract;

import android.content.Context;

import me.zhixingye.salty.basic.BasicPresenter;
import me.zhixingye.salty.basic.BasicView;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */


public class SplashContract {

    public interface View extends BasicView<Presenter> {
        void startLoginActivity();

        void startHomeActivity();

        void startGuideActivity();

        void showLoginError(String error);
    }


    public interface Presenter extends BasicPresenter<View> {

        void checkLoginState(Context context);
    }
}
