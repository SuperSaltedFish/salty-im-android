package me.zhixingye.salty.basic;


import me.zhixingye.salty.widget.listener.Cancelable;
import me.zhixingye.salty.widget.view.SaltyToast;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */

//这里都是一些公共的方法，一般baseActivity或者baseFragment等base都已经实现了
public interface BasicView<P> {
    //显示Loading对话框
    void setDisplayLoading(boolean isEnable);

    //显示Loading对话框，并且Loading是允许取消的
    void setDisplayLoading(boolean isEnable, Cancelable cancelableTask);

    //展示对话框
    void showDialog(String content);

    //展示对话框，含标题
    void showDialog(String title, String content);

    //展示toast
    void showToast(String content, @SaltyToast.ToastType int type, int duration);

    //展示错误信息，默认是toast的方式
    void showError(String error);

    //判断是否已经attach到presenter
    boolean isAttachedToPresenter();
}
