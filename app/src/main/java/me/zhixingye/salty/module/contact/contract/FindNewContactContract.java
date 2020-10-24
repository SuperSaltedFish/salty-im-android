package me.zhixingye.salty.module.contact.contract;


import com.salty.protos.UserProfile;

import me.zhixingye.base.component.mvp.IPresenter;
import me.zhixingye.base.component.mvp.IView;
import me.zhixingye.salty.module.contact.presenter.FindNewContactPresenter;


/**
 * Created by YZX on 2017年11月27日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class FindNewContactContract {
    public interface View extends IView<FindNewContactPresenter> {
        void showStrangerProfile(UserProfile user);

        void showContactProfile(UserProfile user);

        void showSearchNotExist();
    }


    public interface Presenter extends IPresenter<View> {

        void searchUser(String nicknameOrTelephone);
    }
}
