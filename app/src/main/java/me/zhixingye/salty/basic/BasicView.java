package me.zhixingye.salty.basic;


import me.zhixingye.salty.widget.listener.Cancelable;

/**
 * Created by YZX on 2017年10月18日.
 * 其实你找不到错误不代表错误不存在，同样你看不到技术比你牛的人并不代表世界上没有技术比你牛的人。
 */

//这里都是一些公共的方法，一般baseActivity或者baseFragment等base都已经实现了
public interface BasicView<P> {
    //显示Loading对话框
    void setEnableLoading(boolean isEnable);

    //显示Loading对话框，并且Loading是允许取消的
    void setEnableLoading(boolean isEnable, Cancelable cancelableTask);

    //展示对话框
    void showDialog(String content);

    //展示toast
    void showToast(String content);

    //展示错误信息，默认是toast的方式
    void showError(String error);

    //判断是否已经attach到presenter
    boolean isAttachedToPresenter();
}
