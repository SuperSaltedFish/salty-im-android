package me.zhixingye.base.component.mvvm;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import me.zhixingye.base.component.UIComponent;
import me.zhixingye.base.view.SaltyToast;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年01月27日.
 */
public interface MVVMView extends LifecycleOwner, UIComponent {

    default void observeBasic(@NonNull BasicViewModel viewModel) {
        viewModel.getToastHintMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String toastContent) {
                showShortToast(toastContent, SaltyToast.TYPE_HINT);
            }
        });

        viewModel.getToastErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String toastContent) {
                showShortToast(toastContent, SaltyToast.TYPE_ERROR);
            }
        });

        viewModel.getToastSuccessMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String toastContent) {
                showShortToast(toastContent, SaltyToast.TYPE_SUCCESS);
            }
        });

        viewModel.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                showError(error);
            }
        });

        viewModel.getLoadingDialogMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                if (TextUtils.isEmpty(message)) {
                    hideLoadingDialog();
                } else {
                    showLoadingDialog(null, message);
                }
            }
        });

        viewModel.getHintDialogMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                showHintDialog(message);
            }
        });
    }
}
