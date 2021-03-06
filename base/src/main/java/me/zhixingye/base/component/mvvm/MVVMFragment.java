package me.zhixingye.base.component.mvvm;

import android.app.Application;

import androidx.lifecycle.ViewModelProvider;

import me.zhixingye.base.component.BasicFragment;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年10月21日.
 */
public abstract class MVVMFragment extends BasicFragment {

    private ViewModelProvider mViewModelProvider;

    protected <T extends BasicViewModel> T createViewModel(Class<T> cls) {
        if (mViewModelProvider == null) {
            Application application = requireActivity().getApplication();
            ViewModelProvider.Factory factory = new ViewModelProvider.AndroidViewModelFactory(application);
            mViewModelProvider = new ViewModelProvider(this, factory);
        }
        T viewModel = mViewModelProvider.get(cls);
        viewModel.observeBasic(this);
        return viewModel;
    }
}
