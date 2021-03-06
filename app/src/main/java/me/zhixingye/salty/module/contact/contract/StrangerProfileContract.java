package me.zhixingye.salty.module.contact.contract;

import com.salty.protos.ContactOperationMessage;
import com.salty.protos.UserProfile;

import me.zhixingye.base.component.mvp.IPresenter;
import me.zhixingye.base.component.mvp.IView;
import me.zhixingye.salty.module.contact.presenter.StrangerProfilePresenter;


/**
 * Created by YZX on 2018年01月29日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */


public class StrangerProfileContract {

    public interface View extends IView<StrangerProfilePresenter> {
        void goBack();
    }

    public interface Presenter extends IPresenter<View> {

        UserProfile getUserProfileFromLocal(String userId);

        ContactOperationMessage getContactOperationFromLocal(String targetUserId);

        void requestAddContact(String userId, String addReason);

        void acceptAddContact(String userId);

        void refusedAddContact(String userId, String rejectReason);
    }
}
