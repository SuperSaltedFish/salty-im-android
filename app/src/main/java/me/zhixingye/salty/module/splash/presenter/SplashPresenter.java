package me.zhixingye.salty.module.splash.presenter;

import android.content.Context;
import android.os.Handler;

import me.zhixingye.im.sdk.IMClient;
import me.zhixingye.salty.configure.AppConfig;
import me.zhixingye.salty.module.splash.contract.SplashContract;


/**
 * Created by YZX on 2017年11月04日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */

public class SplashPresenter implements SplashContract.Presenter {

    private SplashContract.View mSplashView;

    @Override
    public void attachView(SplashContract.View view) {
        mSplashView = view;
    }

    @Override
    public void detachView() {
        mSplashView = null;
    }

    @Override
    public void checkLoginState(Context context) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (IMClient.get().getAccountService().isLogged()) {
                    mSplashView.startHomeActivity();
                } else {
                    if (AppConfig.isEverStartedGuide()) {
                        mSplashView.startLoginActivity();
                    } else {
                        mSplashView.startGuideActivity();
                    }
                }
            }
        }, 300);//延迟是为了防止进程还没起来就调用IMClient
    }

}
