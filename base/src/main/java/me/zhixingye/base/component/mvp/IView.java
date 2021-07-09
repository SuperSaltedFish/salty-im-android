package me.zhixingye.base.component.mvp;


import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import me.zhixingye.base.component.UIComponent;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */

//这里都是一些公共的方法，一般baseActivity或者baseFragment等base都已经实现了
public interface IView<P extends IPresenter> extends UIComponent {

    //绑定Presenter
    @SuppressWarnings("unchecked")
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    default void bindPresenter() {
        if (isAttachedToPresenter()) {
            throw new RuntimeException("cannot bind Presenter repeatedly");
        }

        try {
            P presenter = PresenterManager.createPresenterFromInterfaceGeneric(this);
            presenter.attachView(this);
            PresenterManager.savePresenter(this, presenter);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to create presenter", e);
        }
    }

    //解除绑定Presenter
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    default void unbindPresenter() {
        P presenter = PresenterManager.getPresenter(this);
        if (presenter != null) {
            PresenterManager.removePresenterFromCache(this);
            presenter.detachView();
        }


    }

    //获取一个已经绑定好的Presenter
    default P getPresenter() {
        return PresenterManager.getPresenter(this);
    }

    //    //判断是否已经attach到presenter
    default boolean isAttachedToPresenter() {
        return getPresenter() != null;
    }


    default void cancelProgressButtonLoadingIfNeed() {

    }

    static void tryBindPresenter(Object mvpView) {
        if (mvpView instanceof IView) {
            IView<?> view = (IView<?>) mvpView;
            view.bindPresenter();
        }
    }

    static void tryUnbindPresenter(Object mvpView) {
        if (mvpView instanceof IView) {
            IView<?> view = (IView<?>) mvpView;
            view.unbindPresenter();
        }
    }

}
