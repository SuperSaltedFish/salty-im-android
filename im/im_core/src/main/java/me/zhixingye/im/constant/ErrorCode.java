package me.zhixingye.im.constant;

import androidx.annotation.Nullable;

/**
 * Created by zhixingye on 2020年01月13日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class ErrorCode {

    public static final ErrorCode INTERNAL_UNKNOWN = new ErrorCode(-1, "未知的错误");
    public static final ErrorCode INTERNAL_UNKNOWN_RESP_DATA = new ErrorCode(-2, "未知的响应数据");

    public static boolean isErrorCode(int code) {
        return code != 200;
    }

    private int code;
    private String msg;

    public ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int hashCode() {
        return ErrorCode.class.hashCode() + code;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof ErrorCode)) {
            return false;
        }
        return ((ErrorCode) obj).code == this.code;
    }


}
