package me.zhixingye.salty.module.splash.viewmodel;

import me.zhixingye.base.component.mvvm.BasicViewModel;
import me.zhixingye.salty.configure.AppConfig;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年03月06日.
 */
public class GuideViewModel extends BasicViewModel {

    public void setEverStartedGuide(boolean EverStartedGuide) {
        AppConfig.setEverStartedGuide(EverStartedGuide);
    }
}
