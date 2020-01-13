package me.zhixingye.im.listener;

import me.zhixingye.im.constant.ErrorCode;

/**
 * Created by zhixingye on 2020年01月10日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public interface RequestCallback<Resp> {
    void onCompleted(Resp response);

    void onFailure(ErrorCode code);
}
