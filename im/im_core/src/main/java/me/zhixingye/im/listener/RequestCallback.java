package me.zhixingye.im.listener;

/**
 * Created by zhixingye on 2020年01月10日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public interface RequestCallback<Resp> {
    void onCompleted(Resp response);

    void onFailure(int code, String error);
}
