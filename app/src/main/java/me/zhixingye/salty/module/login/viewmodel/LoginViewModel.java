package me.zhixingye.salty.module.login.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.salty.protos.ObtainTelephoneSMSCodeResp;
import com.salty.protos.SMSOperationType;
import com.salty.protos.StatusCode;
import com.salty.protos.UserProfile;

import me.zhixingye.base.component.mvvm.BasicViewModel;
import me.zhixingye.base.view.SaltyToast;
import me.zhixingye.im.sdk.IMClient;
import me.zhixingye.salty.widget.listener.MVVMRequestCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年01月27日.
 */
public class LoginViewModel extends BasicViewModel {

    private final MutableLiveData<LoginResult> mLoginSuccessData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mLoginLoadingData = new MutableLiveData<>();

    public void loginByTelephone(String telephone, String password) {
        IMClient.get().getLoginService().loginByTelephone(
                telephone,
                password,
                new MVVMRequestCallback<UserProfile>(this) {
                    @Override
                    protected void setDisplayLoading(boolean isEnable) {
                        postValue(mLoginLoadingData, isEnable);
                    }

                    @Override
                    protected void onSucceed(UserProfile result) {
                        postValue(mLoginSuccessData, new LoginResult(null, true));
                    }

                    @Override
                    protected void showError(int code, String error) {
                        if (code == StatusCode.STATUS_ACCOUNT_AUTHORIZED_REQUIRED.getNumber()) {
                            setToastData(new ToastData(error, SaltyToast.TYPE_HINT));
                        } else {
                            super.showError(code, error);
                        }
                        postValue(mLoginSuccessData, new LoginResult(StatusCode.forNumber(code), false));
                    }
                });
    }

    public LiveData<LoginResult> getLoginSuccessData() {
        return mLoginSuccessData;
    }

    public LiveData<Boolean> getLoginLoadingData() {
        return mLoginLoadingData;
    }

    public static class LoginResult {
        public final StatusCode code;
        public final boolean isLoginSucceed;

        public LoginResult(StatusCode code, boolean isLoginSucceed) {
            this.code = code;
            this.isLoginSucceed = isLoginSucceed;
        }
    }
}
