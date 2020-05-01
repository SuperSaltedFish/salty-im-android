package me.zhixingye.im.listener;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public interface RequestCallback<Resp> {
    void onCompleted(Resp response);

    void onFailure(int code, String error);
}
