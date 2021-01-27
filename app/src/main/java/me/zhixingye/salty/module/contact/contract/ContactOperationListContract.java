package me.zhixingye.salty.module.contact.contract;


import androidx.annotation.Nullable;

import com.salty.protos.ContactOperationMessage;
import com.salty.protos.ContactProfile;
import com.salty.protos.UserProfile;

import java.util.List;

import me.zhixingye.base.component.mvp.IPresenter;
import me.zhixingye.base.component.mvp.IView;
import me.zhixingye.salty.module.contact.presenter.ContactOperationListPresenter;


/**
 * Created by YZX on 2018年01月20日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class ContactOperationListContract {

    public interface View extends IView<ContactOperationListPresenter> {

        void setRefreshing(boolean refreshing);

        void showContactOperation(List<ContactOperationMessage> messageList);
    }


    public interface Presenter extends IPresenter<View> {

        void loadAllContactOperationMessage();

        void loadAllAndMakeAllAsRead();

        void acceptContactRequest(ContactOperationMessage message);

        void refusedContactRequest(ContactOperationMessage message);

        void removeContactOperation(ContactOperationMessage message);

        @Nullable
        ContactProfile getContactBy(String userID);

        boolean isMySelf(UserProfile profile);

    }

}
