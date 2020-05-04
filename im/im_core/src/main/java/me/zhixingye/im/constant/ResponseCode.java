package me.zhixingye.im.constant;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public enum ResponseCode {

    INTERNAL_UNKNOWN(-1, "未知的错误"),
    INTERNAL_UNKNOWN_RESP_DATA(-2, "无法解析响应数据"),
    INTERNAL_ILLICIT_RESP_DATA(-3, "非法的响应数据"),
    INTERNAL_IPC_EXCEPTION(-4, "客户端繁忙，请稍后再试：-3"),

    REMOTE_NEED_LOGIN_AUTH(200402, "本次登录需要验证码校验"),
    REMOTE_USER_ALREADY_REGISTER(200226, "用户已注册");

    public static boolean isErrorCode(int errorCode) {
        return errorCode != 200;
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
