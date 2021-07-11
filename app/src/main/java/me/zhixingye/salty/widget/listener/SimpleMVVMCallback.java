package me.zhixingye.salty.widget.listener;

import androidx.annotation.Nullable;

import me.zhixingye.base.component.mvvm.BasicViewModel;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年07月11日.
 */
public class SimpleMVVMCallback<T> extends MVVMCallback<T> {

    public SimpleMVVMCallback(@Nullable BasicViewModel viewModel) {
        this(viewModel, true);
    }

    public SimpleMVVMCallback(@Nullable BasicViewModel viewModel, boolean isNeedLoading) {
        super(viewModel, isNeedLoading);
    }

    @Override
    protected void onSucceed(T result) {

    }
}
