package me.zhixingye.salty.tool;

import android.text.TextUtils;

import com.salty.protos.ContactProfile;
import com.salty.protos.ContactRemark;
import com.salty.protos.UserProfile;

import java.util.Calendar;
import java.util.Date;

import me.zhixingye.salty.util.PinYinUtil;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年06月05日.
 */
public class UserDataFormatter {

    public static String getContactName(ContactProfile contact) {
        UserProfile user = contact.getUserProfile();
        ContactRemark remark = contact.getRemarkInfo();
        String name = remark.getRemarkName();
        if (TextUtils.isEmpty(name)) {
            name = user.getNickname();
        }
        if (TextUtils.isEmpty(name)) {
            name = user.getUserId().substring(0, 8);
        }
        return name;
    }

    public static String calculateAge(UserProfile profile) {
        Date destDate = new Date(profile.getBirthday());
        Calendar now = Calendar.getInstance();
        if (now.before(destDate)) {
            return "保密";
        }
        int yearNow = now.get(Calendar.YEAR);
        int monthNow = now.get(Calendar.MONTH);
        int dayOfMonthNow = now.get(Calendar.DAY_OF_MONTH);
        now.setTime(destDate);

        int yearDest = now.get(Calendar.YEAR);
        int monthDest = now.get(Calendar.MONTH);
        int dayOfMonthDest = now.get(Calendar.DAY_OF_MONTH);

        int yearInterval = yearNow - yearDest;

        if (monthNow <= monthDest) {
            if (monthNow == monthDest) {
                if (dayOfMonthNow < dayOfMonthDest) {
                    yearInterval--;
                }
            } else {
                yearInterval--;
            }
        }
        return yearInterval + "岁";
    }

    public static String getAbbreviation(ContactProfile contact) {
        return PinYinUtil.getPinYinAbbreviation(getContactName(contact), false);
    }
}
