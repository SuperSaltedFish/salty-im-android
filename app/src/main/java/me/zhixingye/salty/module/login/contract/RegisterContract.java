package me.zhixingye.salty.module.login.contract;


import me.zhixingye.salty.basic.BasicPresenter;
import me.zhixingye.salty.basic.BasicView;

/**
 * Created by YZX on 2018年07月08日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class RegisterContract {
    public interface View extends BasicView<Presenter> {
        void jumpToVerifyPage();
    }


    public interface Presenter extends BasicPresenter<View> {
        void obtainRegisterVerifyCode(String username);
    }
}
