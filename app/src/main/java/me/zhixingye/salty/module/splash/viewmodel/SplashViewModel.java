package me.zhixingye.salty.module.splash.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.salty.protos.UserProfile;

import me.zhixingye.base.component.mvvm.BasicViewModel;
import me.zhixingye.im.sdk.IMClient;
import me.zhixingye.salty.configure.AppConfig;
import me.zhixingye.salty.widget.listener.MVVMRequestCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年02月04日.
 */
public class SplashViewModel extends BasicViewModel {

    public static final int STATE_LOGGED = 1;
    public static final int STATE_LOGOUT = 2;
    public static final int STATE_LOGOUT_FIRST = 3;

    private final MutableLiveData<Integer> mLoginStateData = new MutableLiveData<>();

    public void checkLoginState() {
        IMClient.get().waitInitFinish(new IMClient.OnInitFinishListener() {
            @Override
            public void onFinish() {
                if (IMClient.get().getLoginService().isLogged()) {
                    postValue(mLoginStateData, STATE_LOGGED);
                } else {
                    IMClient.get().getLoginService().loginByLastLoginInfo(new MVVMRequestCallback<UserProfile>(SplashViewModel.this) {
                        @Override
                        protected void setDisplayLoading(boolean isEnable) {
                        }

                        @Override
                        protected void onSucceed(UserProfile result) {
                            postValue(mLoginStateData, STATE_LOGGED);
                        }

                        @Override
                        protected void showError(int code, String error) {
                            if (AppConfig.isEverStartedGuide()) {
                                postValue(mLoginStateData, STATE_LOGOUT);
                            } else {
                                postValue(mLoginStateData, STATE_LOGOUT_FIRST);
                            }
                        }

                    });
                }
            }
        });
    }

    public LiveData<Integer> getLoginStateData() {
        return mLoginStateData;
    }
}
