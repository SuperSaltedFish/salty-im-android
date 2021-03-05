package me.zhixingye.salty.module.login.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.salty.protos.RegisterResp;
import com.salty.protos.ResetPasswordResp;

import me.zhixingye.base.component.mvvm.BasicViewModel;
import me.zhixingye.im.sdk.IMClient;
import me.zhixingye.salty.widget.listener.MVPRequestCallback;
import me.zhixingye.salty.widget.listener.MVVMRequestCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年02月04日.
 */
public class ResetLoginPasswordViewModel extends BasicViewModel {

    private final MutableLiveData<Boolean> mRegisterSuccessData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mResetTelephoneSuccessData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mLoadingData = new MutableLiveData<>();

    public void registerByTelephone(String telephone, String password) {
        IMClient.get().getRegisterService().registerByTelephone(
                telephone,
                password,
                new MVVMRequestCallback<RegisterResp>(this) {
                    @Override
                    protected void onSucceed(RegisterResp result) {
                        postValue(mRegisterSuccessData, true);
                    }

                    @Override
                    protected void setDisplayLoading(boolean isEnable) {
                        postValue(mLoadingData, isEnable);
                    }
                });
    }

    public void resetTelephoneLoginPassword(String telephone, String newPassword) {
        IMClient.get().getPasswordService().resetLoginPasswordByTelephone(
                telephone,
                newPassword,
                new MVVMRequestCallback<ResetPasswordResp>(this) {
                    @Override
                    protected void onSucceed(ResetPasswordResp result) {
                        postValue(mResetTelephoneSuccessData, true);
                    }

                    @Override
                    protected void setDisplayLoading(boolean isEnable) {
                        postValue(mLoadingData, isEnable);
                    }
                });
    }

    public LiveData<Boolean> getRegisterSuccessData() {
        return mRegisterSuccessData;
    }

    public LiveData<Boolean> getResetTelephoneSuccessData() {
        return mResetTelephoneSuccessData;
    }

    public LiveData<Boolean> getLoadingData() {
        return mLoadingData;
    }
}
