package me.zhixingye.salty.module.splash.contract;

import android.content.Context;

import me.zhixingye.salty.basic.BasicPresenter;
import me.zhixingye.salty.basic.BasicView;

/**
 * Created by YZX on 2017年11月04日.
 * 每一个不曾起舞的日子 都是对生命的辜负
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
