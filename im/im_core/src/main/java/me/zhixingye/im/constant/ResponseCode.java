package me.zhixingye.im.constant;

/**
 * Created by zhixingye on 2020年01月13日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public enum ResponseCode {

    INTERNAL_UNKNOWN(-1, "未知的错误"),
    INTERNAL_UNKNOWN_RESP_DATA(-2, "未知的响应数据"),
    INTERNAL_IPC_EXCEPTION(-3, "客户端繁忙，请稍后再试：-3");

    public static boolean isErrorCode(int errorCode) {
        return errorCode <= 200;
    }

    private int code;
    private String msg;

    private ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "ErrorCode{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
