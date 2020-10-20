package me.zhixingye.base.component.mvp;


import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import me.zhixingye.base.component.UIComponent;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */

//这里都是一些公共的方法，一般baseActivity或者baseFragment等base都已经实现了
public interface IView<P extends IPresenter>
        extends UIComponent, LifecycleObserver {

    @NonNull
    P createPresenterImpl();

    void onPresenterBound();

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    default void bindPresenter() {
        P presenter = createPresenterImpl();
        presenter.attachView(this);
        PresenterCache.savePresenter(hashCode(), presenter);
        onPresenterBound();
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    default void unBindPresenter() {
        P presenter = getPresenter();
        if (presenter != null) {
            PresenterCache.removePresenterFromCache(hashCode());
            presenter.detachView();
        }
    }

    default P getPresenter() {
        return PresenterCache.getPresenter(hashCode());
    }

    //    //判断是否已经attach到presenter
    default boolean isAttachedToPresenter() {
        return getPresenter() != null;
    }

    default void cancelProgressButtonLoadingIfNeed() {

    }

}
