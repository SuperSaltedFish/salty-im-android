package me.zhixingye.salty.module.login.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年01月27日.
 */
public class LoginViewModel extends ViewModel {

    private MutableLiveData<Boolean> mLoginSuccessState;

    public synchronized MutableLiveData<Boolean> getLoginSuccessState() {
        if (mLoginSuccessState == null) {
            mLoginSuccessState = new MutableLiveData<>();
            mLoginSuccessState.setValue(false);
        }
        return mLoginSuccessState;
    }

    public void login(){

    }
}
