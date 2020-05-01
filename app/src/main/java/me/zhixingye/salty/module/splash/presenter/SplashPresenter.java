package me.zhixingye.salty.module.splash.presenter;

import android.content.Context;
import android.os.Handler;

import me.zhixingye.im.sdk.IMClient;
import me.zhixingye.im.tool.Logger;
import me.zhixingye.salty.configure.AppConfig;
import me.zhixingye.salty.module.splash.contract.SplashContract;


/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
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
                IMClient.get().getStorageService().putToConfigurationPreferences("22", "22");
                Logger.e("sssssssssssss",  IMClient.get().getStorageService().getFromConfigurationPreferences("22")); ;
            }
        }, 300);//延迟是为了防止进程还没起来就调用IMClient
    }

}
