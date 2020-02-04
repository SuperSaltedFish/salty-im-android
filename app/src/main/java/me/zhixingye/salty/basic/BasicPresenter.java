package me.zhixingye.salty.basic;


/**
 * Created by YZX on 2017年10月18日.
 * 其实你找不到错误不代表错误不存在，同样你看不到技术比你牛的人并不代表世界上没有技术比你牛的人。
 */


public interface BasicPresenter<V extends BasicView> {

    //Presenter绑定View，一般在activity的onCreated()会调用
    void attachView(V v);

    //Presenter解绑View，一般在activity的onDestroy()会调用
    void detachView();
}
