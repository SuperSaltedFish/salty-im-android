package me.zhixingye.base.component;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import me.zhixingye.base.dialog.ProgressDialogFragment;
import me.zhixingye.base.view.SaltyToast;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年10月04日.
 */
public interface UIComponent {

    //获取context
    Context getContext();

    //关闭当前页面
    default void closeCurrentPage() {
        if (this instanceof Activity) {
            ((Activity) this).finish();
        } else if (this instanceof DialogFragment) {
            ((DialogFragment) this).dismissAllowingStateLoss();
        } else if (this instanceof Fragment) {
            Activity activity = ((Fragment) this).getActivity();
            if (activity != null) {
                activity.finish();
            }
        }
    }

    //显示Loading对话框，没有tag
    default void showLoadingDialog() {
        showLoadingDialog(null);
    }

    //显示Loading对话框，支持tag
    default void showLoadingDialog(String text) {
        showLoadingDialog(text, false, null);
    }

    //显示Loading对话框,带自定义loading文字,是否可以关闭，关闭的回调事件
    default void showLoadingDialog(String text, boolean isCancelable, @Nullable DialogInterface.OnDismissListener listener) {
        Context context = getContext();
        if (!(context instanceof FragmentActivity)) {
            return;
        }
        hideSoftKeyboard();
        FragmentActivity fActivity = (FragmentActivity) context;
        ProgressDialogFragment progressDialog = ProgressDialogFragment.create(text, isCancelable);
        progressDialog.setSingleInstance(true);
        progressDialog.setOnDismissListener(listener);
        progressDialog.showNow(fActivity.getSupportFragmentManager(), toString());
    }

    //隐藏Loading对话框
    default void hideLoadingDialog() {
        Context context = getContext();
        if (!(context instanceof FragmentActivity)) {
            return;
        }

        FragmentActivity fActivity = (FragmentActivity) context;
        FragmentManager manager = fActivity.getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(toString());
        if (fragment instanceof ProgressDialogFragment) {
            ((ProgressDialogFragment) fragment).dismissAllowingStateLoss();
        }

    }

    //展示对话框
    default void showHintDialog(String content) {
        showHintDialog(null, content, null);
    }

    //展示对话框
    default void showHintDialog(String content, @Nullable DialogInterface.OnDismissListener listener) {
        showHintDialog(null, content, listener);
    }

    //展示对话框，含标题
    default void showHintDialog(String title, String content, @Nullable DialogInterface.OnDismissListener listener) {
        Context context = getContext();
        if (context == null) {
            return;
        }
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(content)
                .setOnDismissListener(listener)
                .setPositiveButton("确认", null)
                .show();
    }

    //展示ShortToast
    default void showShortToast(String content, @SaltyToast.ToastType int type) {
        showToast(content, type, 4000);
    }

    //展示LongToast
    default void showLongToast(String content, @SaltyToast.ToastType int type) {
        showToast(content, type, 7000);
    }

    //展示toast
    default void showToast(String content, @SaltyToast.ToastType int type, int duration) {
        Context context = getContext();
        if (context == null) {
            return;
        }
        SaltyToast.showToast(context, content, type, duration);
    }

    //展示错误信息，默认是toast的方式
    default void showError(String error) {
        if (TextUtils.isEmpty(error)) {
            return;
        }
        showLongToast(error, SaltyToast.TYPE_ERROR);
        vibrate(50);
    }

    //调用手机震动
    default void vibrate(long milliseconds) {
        Context context = getContext();
        if (context == null) {
            return;
        }
        Vibrator vibrator = context.getSystemService(Vibrator.class);
        vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
    }

    //设置亮度
    default void setScreenBrightness(@FloatRange(from = 0, to = 1) float paramFloat) {
        Context context = getContext();
        if (!(context instanceof Activity)) {
            return;
        }
        Activity activity = (Activity) context;
        int systemBrightness;
        try {
            systemBrightness = Settings.System
                    .getInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            systemBrightness = 0;
        }

        Window window = activity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        if (paramFloat * 255 < systemBrightness || paramFloat < params.screenBrightness) {
            return;
        }
        params.screenBrightness = paramFloat;
        window.setAttributes(params);
    }

    //强制开启键盘
    default void showSoftKeyboard(final View focusView) {//一定要等界面可见才可以弹出键盘，否则无效
        Context context = getContext();
        if (context == null) {
            return;
        }
        InputMethodManager manager = context.getSystemService(InputMethodManager.class);
        if (manager == null) {
            return;
        }
        //但如果View没有测量，要等View测量之后才弹出键盘，不然有时候键盘会弹不出
        focusView.post(() -> {
            if (!focusView.isFocusable()) {
                return;
            }
            focusView.requestFocus();
            manager.showSoftInput(focusView, 0);
        });
    }

    //隐藏软键盘
    default void hideSoftKeyboard() {
        Context context = getContext();
        if (!(context instanceof Activity)) {
            return;
        }
        Activity activity = (Activity) context;
        InputMethodManager manager = activity.getSystemService(InputMethodManager.class);
        if (manager == null) {
            return;
        }
        if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            View focusView = activity.getCurrentFocus();
            if (focusView != null) {
                focusView.clearFocus();
                manager.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            }
        }
    }
}
