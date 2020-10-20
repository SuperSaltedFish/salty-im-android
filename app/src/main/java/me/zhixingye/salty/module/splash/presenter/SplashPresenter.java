package me.zhixingye.salty.module.splash.presenter;

import android.content.Context;
import android.os.Handler;

import com.salty.protos.UserProfile;

import me.zhixingye.im.sdk.IMClient;
import me.zhixingye.salty.configure.AppConfig;
import me.zhixingye.salty.module.splash.contract.SplashContract;
import me.zhixingye.salty.widget.listener.LifecycleMVPRequestCallback;


/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */

public class SplashPresenter implements SplashContract.Presenter {

    private SplashContract.View mView;

    @Override
    public void attachView(SplashContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void checkLoginState(Context context) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (IMClient.get().getAccountService().isLogged()) {
                    mView.startHomeActivity();
                } else {
                    IMClient.get().getAccountService().loginByLastLoginInfo(new LifecycleMVPRequestCallback<UserProfile>(mView, false) {
                        @Override
                        protected void onSuccess(UserProfile result) {
                            mView.startHomeActivity();
                        }

                        @Override
                        protected boolean onError(int code, String error) {
                            if (AppConfig.isEverStartedGuide()) {
                                mView.startLoginActivity();
                            } else {
                                mView.startGuideActivity();
                            }
                            return true;
                        }
                    });
                }
            }
        }, 1000);//延迟是为了防止进程还没起来就调用IMClient
    }

}
