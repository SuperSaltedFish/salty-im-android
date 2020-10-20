package me.zhixingye.base.component.mvp;


/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public interface IPresenter<V extends IView<?>> {


    //Presenter绑定View，一般在activity的onCreated()会调用
    void attachView(V v);

    //Presenter解绑View，一般在activity的onDestroy()会调用
    void detachView();
}
