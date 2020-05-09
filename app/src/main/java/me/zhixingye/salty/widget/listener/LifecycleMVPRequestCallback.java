package me.zhixingye.salty.widget.listener;

import java.lang.ref.WeakReference;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.salty.basic.BasicView;


/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public abstract class LifecycleMVPRequestCallback<T> implements RequestCallback<T> {

    private WeakReference<BasicView> mLifecycleView;
    private boolean isEnableLoading;//自动启动和关闭加载对话框

    public LifecycleMVPRequestCallback(BasicView lifecycleView) {
        this(lifecycleView, true);
    }

    public LifecycleMVPRequestCallback(BasicView lifecycleView, boolean isEnableLoading) {
        this(lifecycleView, isEnableLoading, false);
    }

    public LifecycleMVPRequestCallback(
            BasicView lifecycleView,
            boolean isEnableLoading,
            boolean isEnableCancelableDialog) {
        mLifecycleView = new WeakReference<>(lifecycleView);
        this.isEnableLoading = isEnableLoading;
        if (isEnableLoading && lifecycleView != null) {
            lifecycleView.setDisplayLoadingDialog(true,
                    isEnableCancelableDialog ? DEFAULT_CANCELABLE : null);
        }
    }

    protected abstract void onSuccess(T result);

    protected boolean onError(int code, String error) {
        return false;
    }

    @Override
    public void onCompleted(T response) {
        BasicView view = mLifecycleView.get();
        if (view != null && view.isAttachedToPresenter()) {
            if (isEnableLoading && isEnableDismissLoadingInSuccessful()) {
                view.setDisplayLoadingDialog(false);
            }
            if (isEnableCancelProgressButtonLoadingOnSuccessful()) {
                view.cancelProgressButtonLoadingIfNeed();
            }
            onSuccess(response);
        }
    }

    @Override
    public void onFailure(int code, String error) {
        BasicView view = mLifecycleView.get();
        if (view != null && view.isAttachedToPresenter()) {
            if (isEnableLoading) {
                view.setDisplayLoadingDialog(false);
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

    private static final Cancelable DEFAULT_CANCELABLE = new Cancelable() {
        @Override
        public void cancel() {

        }
    };
}
