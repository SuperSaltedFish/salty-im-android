package me.zhixingye.salty.module.login.contract;


import me.zhixingye.salty.basic.BasicPresenter;
import me.zhixingye.salty.basic.BasicView;

/**
 * Created by YZX on 2018年07月09日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class VerifyContract {
    public interface View extends BasicView<Presenter> {

        void showCountDown();

        void startHomeActivity();

        void jumpToLoginPage();
    }


    public interface Presenter extends BasicPresenter<View> {

        void login(String username, String password, String verifyCode);

        void register(String username, String password, String nickname, String verifyCode);

        void obtainLoginSMS(String username, String password);

        void obtainRegisterSMS(String username);

    }
}
