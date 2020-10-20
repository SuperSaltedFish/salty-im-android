package me.zhixingye.salty.module.splash.contract;

import android.content.Context;

import me.zhixingye.base.component.mvp.IPresenter;
import me.zhixingye.base.component.mvp.IView;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class SplashContract {

    public interface View extends IView<Presenter> {
        void startLoginActivity();

        void startHomeActivity();

        void startGuideActivity();

        void showLoginError(String error);
    }


    public interface Presenter extends IPresenter<View> {

        void checkLoginState(Context context);
    }
}
