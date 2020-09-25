package me.zhixingye.salty.module.contact.contract;


import com.salty.protos.UserProfile;

import me.zhixingye.salty.basic.BasicPresenter;
import me.zhixingye.salty.basic.BasicView;

/**
 * Created by YZX on 2017年11月27日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class FindNewContactContract {
    public interface View extends BasicView<Presenter> {
        void showStrangerProfile(UserProfile user);

        void showContactProfile(UserProfile user);

        void showSearchNotExist();
    }


    public interface Presenter extends BasicPresenter<View> {

        void searchUser(String nicknameOrTelephone);
    }
}
