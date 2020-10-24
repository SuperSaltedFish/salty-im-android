package me.zhixingye.base.component.mvp;

import android.os.Bundle;

import me.zhixingye.base.component.BasicActivity;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年10月10日.
 */
public abstract class MVPBasicActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        IView.tryBindPresenter(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IView.tryUnbindPresenter(this);
    }


}
