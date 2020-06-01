package me.zhixingye.salty.module.login.presenter;

import me.zhixingye.salty.module.login.contract.RegisterContract;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class RegisterPresenter implements RegisterContract.Presenter {

    private RegisterContract.View mRegisterView;

    @Override
    public void attachView(RegisterContract.View view) {
        mRegisterView = view;
    }

    @Override
    public void detachView() {
        mRegisterView = null;
    }

}
