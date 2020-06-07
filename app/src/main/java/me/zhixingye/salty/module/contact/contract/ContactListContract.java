package me.zhixingye.salty.module.contact.contract;

import com.salty.protos.ContactProfile;

import java.util.List;

import me.zhixingye.salty.basic.BasicPresenter;
import me.zhixingye.salty.basic.BasicView;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年06月07日.
 */
public class ContactListContract {
    public interface View extends BasicView<Presenter> {
        void updateUnreadBadge(int unreadCount);

        void showContactList(List<ContactProfile> contactList);

        void showTagCount(int count);
    }


    public interface Presenter extends BasicPresenter<View> {
        void loadUnreadCount();

        void loadAllContact();

        void loadTagCount();

    }
}
