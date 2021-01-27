package me.zhixingye.base.component.mvvm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年01月27日.
 */
public class BasicViewModel extends ViewModel {

    protected MutableLiveData<String> mToastHintMessage = new MutableLiveData<>();
    protected MutableLiveData<String> mToastErrorMessage = new MutableLiveData<>();
    protected MutableLiveData<String> mToastSuccessMessage = new MutableLiveData<>();
    protected MutableLiveData<String> mErrorMessage = new MutableLiveData<>();
    protected MutableLiveData<String> mLoadingDialogMessage = new MutableLiveData<>();
    protected MutableLiveData<String> mHintDialogMessage = new MutableLiveData<>();

    public LiveData<String> getToastHintMessage() {
        return mToastHintMessage;
    }

    public LiveData<String> getToastErrorMessage() {
        return mToastErrorMessage;
    }

    public LiveData<String> getToastSuccessMessage() {
        return mToastSuccessMessage;
    }

    public LiveData<String> getErrorMessage() {
        return mErrorMessage;
    }

    public LiveData<String> getLoadingDialogMessage() {
        return mLoadingDialogMessage;
    }

    public LiveData<String> getHintDialogMessage() {
        return mHintDialogMessage;
    }
}
