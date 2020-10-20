package me.zhixingye.salty.widget.listener;


import java.lang.ref.WeakReference;

import me.zhixingye.base.component.mvp.IView;
import me.zhixingye.im.listener.RequestCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public abstract class LifecycleMVPRequestCallback<T> implements RequestCallback<T> {

    private WeakReference<IView<?>> mLifecycleView;
    private boolean isEnableLoading;//自动启动和关闭加载对话框

    public LifecycleMVPRequestCallback(IView<?> lifecycleView) {
        this(lifecycleView, true);
    }

    public LifecycleMVPRequestCallback(IView<?> lifecycleView, boolean isEnableLoading) {
        this(lifecycleView, isEnableLoading, false);
    }

    public LifecycleMVPRequestCallback(
            IView<?> lifecycleView,
            boolean isEnableLoading,
            boolean isEnableCancelableDialog) {
        mLifecycleView = new WeakReference<>(lifecycleView);
        this.isEnableLoading = isEnableLoading;
        if (isEnableLoading && lifecycleView != null) {
            lifecycleView.showLoadingDialog(null, null, isEnableCancelableDialog, null);
        }
    }

    protected abstract void onSuccess(T result);

    protected boolean onError(int code, String error) {
        return false;
    }

    @Override
    public void onCompleted(T response) {
        IView<?> view = mLifecycleView.get();
        if (view != null && view.isAttachedToPresenter()) {
            if (isEnableLoading && isEnableDismissLoadingInSuccessful()) {
                view.hideLoadingDialog();
            }
            if (isEnableCancelProgressButtonLoadingOnSuccessful()) {
                view.cancelProgressButtonLoadingIfNeed();
            }
            onSuccess(response);
        }
    }

    @Override
    public void onFailure(int code, String error) {
        IView<?> view = mLifecycleView.get();
        if (view != null && view.isAttachedToPresenter()) {
            if (isEnableLoading) {
                view.hideLoadingDialog();
            }
            view.cancelProgressButtonLoadingIfNeed();
            if (!onError(code, error)) {
                view.showError(error);
            }
            mLifecycleView.clear();
        }
    }

    public boolean isEnableDismissLoadingInSuccessful() {
        return true;
    }

    public boolean isEnableCancelProgressButtonLoadingOnSuccessful() {
        return true;
    }

}
