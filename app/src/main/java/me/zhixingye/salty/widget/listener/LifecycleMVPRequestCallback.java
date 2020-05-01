package me.zhixingye.salty.widget.listener;

import java.lang.ref.WeakReference;

import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.salty.basic.BasicView;


/**
 * Created by YZX on 2018年12月12日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public abstract class LifecycleMVPRequestCallback<T> implements RequestCallback<T> {

    private WeakReference<BasicView> mLifecycleView;
    private boolean isEnableLoading;//自动启动和关闭加载对话框
    private boolean isAutoDismissLoadingInSuccessful;//是否再返回成功的时候关闭加载对话框，用于一些多个接口连续掉用而只启动一个加载对话框的场景

    public LifecycleMVPRequestCallback(BasicView lifecycleView) {
        this(lifecycleView, true);
    }

    public LifecycleMVPRequestCallback(BasicView lifecycleView, boolean isEnableLoading) {
        this(lifecycleView, isEnableLoading, true);
    }

    public LifecycleMVPRequestCallback(BasicView lifecycleView, boolean isEnableLoading, boolean isAutoDismissLoadingInSuccessful) {
        mLifecycleView = new WeakReference<>(lifecycleView);
        this.isEnableLoading = isEnableLoading;
        this.isAutoDismissLoadingInSuccessful = isAutoDismissLoadingInSuccessful;
        if (isEnableLoading && lifecycleView != null) {
            lifecycleView.setEnableLoading(true);
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
            if (isEnableLoading && isAutoDismissLoadingInSuccessful) {
                view.setEnableLoading(false);
            }
            onSuccess(response);
        }
    }

    @Override
    public void onFailure(int code, String error) {
        BasicView view = mLifecycleView.get();
        if (view != null && view.isAttachedToPresenter()) {
            if (isEnableLoading) {
                view.setEnableLoading(false);
            }
            if (!onError(code, error)) {
                view.showError(error);
            }
            mLifecycleView.clear();
        }
    }
}
