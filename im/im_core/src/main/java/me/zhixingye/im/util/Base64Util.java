package me.zhixingye.im.util;

import android.text.TextUtils;
import android.util.Base64;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */


public class Base64Util {

    public static byte[] decode(byte[] data) {
        if (data == null) {
            return null;
        }
        return Base64.decode(data, Base64.NO_WRAP);
    }

    public static byte[] decode(String data) {
        if (TextUtils.isEmpty(data)) {
            return null;
        }
        return Base64.decode(data, Base64.NO_WRAP);
    }

    public static byte[] encode(byte[] data) {
        if (data == null) {
            return null;
        }
        return Base64.encode(data, Base64.NO_WRAP);
    }

    public static byte[] encode(String data) {
        if (TextUtils.isEmpty(data)) {
            return null;
        }
        return encode(data.getBytes());
    }

    public static String encodeToString(byte[] data) {
        if (data == null) {
            return null;
        }
        return Base64.encodeToString(data, Base64.NO_WRAP);
    }

    public static String encodeToString(String data) {
        if (TextUtils.isEmpty(data)) {
            return null;
        }
        return encodeToString(data.getBytes());
    }
}
