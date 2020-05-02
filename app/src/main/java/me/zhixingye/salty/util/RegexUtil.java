package me.zhixingye.salty.util;

import android.text.TextUtils;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by YZX on 2017年10月19日.
 * 每一个不曾起舞的日子，都是对生命的辜负。
 */


public class RegexUtil {

    /**
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     *
     * @param mobile 移动、联通、电信运营商的号码段
     *               <p>移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
     *               、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）</p>
     *               <p>联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）</p>
     *               <p>电信的号段：133、153、180（未启用）、189</p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isMobile(CharSequence mobile) {
        String regex = "(\\+\\d+)?1[3458]\\d{9}$";
        return Pattern.matches(regex, mobile);
    }

    public static boolean isEmail(CharSequence email) {
        String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        return Pattern.matches(regex, email);
    }

    public static boolean isLegalPassword(CharSequence nickname) {
        String regex = "^[a-zA-Z0-9]{8,16}$";
        return Pattern.matches(regex, nickname);
    }

    public static boolean isCharacter(CharSequence nickname) {
        String regex = "[a-zA-Z]";
        return Pattern.matches(regex, nickname);
    }

    public static boolean isRepeatNumber(CharSequence number) {
        String regex = "([0-9])\\1{5}";
        return Pattern.matches(regex, number);
    }

    public static boolean isChinese(String nickname) {
        String regex = "[\u4e00-\u9fa5]";
        return Pattern.matches(regex, nickname);
    }

    public static boolean isContainsNumbersOrLettersOrSymbol(CharSequence str) {
        int i = 0;
        if (Pattern.matches(".*[0-9].*", str)) {
            i++;
        }
        if (Pattern.matches(".*[a-zA-Z]+.*", str)) {
            i++;
        }
        if (Pattern.matches(".*[!\"#$%&'()*+,-./:;<=>?@^_`{|}~\\[\\]\\\\]+.*", str)) {
            i++;
        }
        return i >= 2;
    }

    public static boolean isRepeatedNumber(CharSequence str, int repeatedLength) {
        String regex = String.format(
                Locale.getDefault(),
                "^.*(\\d)\\1{%d}.*$",
                repeatedLength);
        return Pattern.matches(regex, str);
    }

}
