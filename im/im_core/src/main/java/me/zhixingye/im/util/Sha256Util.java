package me.zhixingye.im.util;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.annotation.Nullable;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class Sha256Util {

    public static String sha256WithSalt(String string, @Nullable String salt) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        if (!TextUtils.isEmpty(salt)) {
            string = string + salt;
        }
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("SHA-256").digest(string.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, SHA-256 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
}
