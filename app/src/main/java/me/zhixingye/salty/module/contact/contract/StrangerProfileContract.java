package me.zhixingye.salty.module.contact.contract;

import com.salty.protos.ContactOperationMessage;
import com.salty.protos.UserProfile;

import me.zhixingye.base.component.mvp.IPresenter;
import me.zhixingye.base.component.mvp.IView;


/**
 * Created by YZX on 2018年01月29日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */


public class StrangerProfileContract {

    public interface View extends IView<Presenter> {
        void goBack();
    }

    public interface Presenter extends IPresenter<View> {

        UserProfile getLocalCacheUserProfile(String userId);

        ContactOperationMessage getContactOperationMessage(String messageId);

        void requestAddContact(String userId, String addReason);

        void acceptAddContact(String userId);

        void refusedAddContact(String userId, String rejectReason);
    }
}
