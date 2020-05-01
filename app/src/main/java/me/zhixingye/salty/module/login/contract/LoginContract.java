package me.zhixingye.salty.module.login.contract;


import me.zhixingye.salty.basic.BasicPresenter;
import me.zhixingye.salty.basic.BasicView;

/**
 * Created by YZX on 2017年10月18日.
 * 其实你找不到错误不代表错误不存在，同样你看不到技术比你牛的人并不代表世界上没有技术比你牛的人。
 */


public class LoginContract {

    public interface View extends BasicView<Presenter> {

        void jumpToVerifyPage();

        void startHomeActivity();
    }


    public interface Presenter extends BasicPresenter<View> {
        void tryLogin(String username, String password);
    }


}
