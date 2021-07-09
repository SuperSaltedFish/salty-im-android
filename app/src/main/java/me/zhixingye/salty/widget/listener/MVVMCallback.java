package me.zhixingye.salty.widget.listener;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

import me.zhixingye.base.component.mvvm.BasicViewModel;
import me.zhixingye.im.listener.RequestCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年01月27日.
 */
public abstract class MVVMCallback<T> implements RequestCallback<T> {

    protected abstract void onSucceed(T result);

    private final WeakReference<BasicViewModel> mBasicViewModel;

    public MVVMCallback(@Nullable BasicViewModel viewModel) {
        this.mBasicViewModel = new WeakReference<>(viewModel);
        onPreRequest();
    }

    @CallSuper
    protected void onPreRequest() {
        setShowLoading(true);
    }

    @CallSuper
    protected void onRequestFinish() {
        setShowLoading(false);
        mBasicViewModel.clear();
    }

    @Override
    public final void onCompleted(T response) {
        onSucceed(response);
        onRequestFinish();
    }

    @CallSuper
    @Override
    public void onFailure(int code, String error) {
        showError(code, error);
        onRequestFinish();
    }

    protected void setShowLoading(boolean isShow) {
        BasicViewModel viewModel = mBasicViewModel.get();
        if (viewModel == null) {
            return;
        }
        if (isShow) {
            BasicViewModel.LoadingDialogData data = new BasicViewModel.LoadingDialogData(null, false, null);
            viewModel.setLoadingDialogData(data);
        } else {
            viewModel.setLoadingDialogData(null);
        }
    }

    protected void showError(int code, String error) {
        BasicViewModel viewModel = mBasicViewModel.get();
        if (viewModel == null) {
            return;
        }
        viewModel.setErrorData(new BasicViewModel.ErrorData(error));
    }
}