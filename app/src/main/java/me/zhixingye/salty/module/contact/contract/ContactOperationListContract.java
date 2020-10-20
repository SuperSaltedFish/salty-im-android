package me.zhixingye.salty.module.contact.contract;


import com.salty.protos.ContactOperationMessage;
import com.salty.protos.ContactProfile;

import java.util.List;

import me.zhixingye.base.component.mvp.IPresenter;
import me.zhixingye.base.component.mvp.IView;


/**
 * Created by YZX on 2018年01月20日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class ContactOperationListContract {

    public interface View extends IView<Presenter> {
        void showContactOperation(List<ContactOperationMessage> messageList);
    }


    public interface Presenter extends IPresenter<View> {

        List<ContactOperationMessage> getAllContactOperationMessage();

        void loadAllAndMakeAllAsRead();

        void acceptContactRequest(ContactOperationMessage message);

        void refusedContactRequest(ContactOperationMessage message);

        void removeContactOperation(ContactOperationMessage message);

        ContactProfile getContactBy(String userID);

    }

}
