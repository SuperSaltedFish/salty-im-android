package me.zhixingye.im.constant;

import com.salty.protos.StatusCode;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public enum ResponseCode {

    INTERNAL_UNKNOWN(-101, "未知的错误"),
    INTERNAL_UNKNOWN_RESP_DATA(-102, "无法解析响应数据"),
    INTERNAL_ILLICIT_RESP_DATA(-103, "非法的响应数据"),
    INTERNAL_IPC_EXCEPTION(-104, "客户端繁忙，请稍后再试"),

    INTERNAL_USER_NOT_LOGGED_IN(-201, "用户未登录，请登陆后再试"),

    REMOTE_NEED_LOGIN_AUTH(StatusCode.STATUS_ACCOUNT_AUTHORIZED_REQUIRED.getNumber(), "本次登录需要验证码校验"),
    REMOTE_USER_ALREADY_REGISTER(StatusCode.STATUS_ACCOUNT_EXISTS.getNumber(), "用户已注册");

    public static boolean isErrorCode(StatusCode code) {
        return code != StatusCode.STATUS_OK;
    }

    private int code;
    private String msg;

    ResponseCode(int code, String msg) {
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
