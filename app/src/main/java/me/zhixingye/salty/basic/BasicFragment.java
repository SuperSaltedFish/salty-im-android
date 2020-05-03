package me.zhixingye.salty.basic;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import me.zhixingye.salty.R;
import me.zhixingye.salty.widget.dialog.AlertDialog;
import me.zhixingye.salty.widget.dialog.ProgressDialog;
import me.zhixingye.salty.widget.listener.Cancelable;
import me.zhixingye.salty.widget.view.SaltyToast;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */


public abstract class BasicFragment<P extends BasicPresenter> extends Fragment {

    private static final String TAG = "BasicFragment";

    @LayoutRes
    protected abstract int getLayoutID();

    protected abstract void init(View parentView);

    protected abstract void setup(Bundle savedInstanceState);

    protected P mPresenter;

    public Context mContext;
    private InputMethodManager mInputManager;
    private boolean isOnceVisible;
    private ProgressDialog mProgressDialog;
    private int mCurrentPermissionsRequestCode;
    private Vibrator mVibrator;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        mVibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
    }

    //Fragment第一次可见
    protected void onFirstVisible() {
    }

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutID(), container, false);
    }

    @Override
    public final void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPresenter();
        init(view);
        setup(savedInstanceState);
        //判断这个Fragment是否第一次显示
        if (getUserVisibleHint() && !isOnceVisible) {
            isOnceVisible = true;
            onFirstVisible();
        }
    }


    @CallSuper
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //判断这个Fragment是否第一次显示
        if (!isOnceVisible && isVisibleToUser) {
            isOnceVisible = true;
            onFirstVisible();
        }
    }

    //显示软键盘
    public void showSoftKeyboard(View focusView) {
        if (mContext != null) {
            if (mInputManager == null) {
                mInputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            }
            focusView.requestFocus();
            mInputManager.showSoftInput(focusView, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    //隐藏软键盘
    public void hideSoftKeyboard() {
        if (mContext instanceof Activity) {
            Activity activity = (Activity) mContext;
            if (mInputManager == null) {
                mInputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            }
            if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
                if (activity.getCurrentFocus() != null)
                    mInputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public void vibrate(long milliseconds) {
        mVibrator.vibrate(milliseconds);
    }

    public void showShortToast(String content, @SaltyToast.ToastType int type) {
        showToast(content, type, Toast.LENGTH_SHORT);
    }

    public void showLongToast(String content, @SaltyToast.ToastType int type) {
        showToast(content, type, Toast.LENGTH_LONG);
    }

    public void showToast(String content, @SaltyToast.ToastType int type, int duration) {
        SaltyToast.showToast(content, type, duration);
    }

    public void setDisplayLoading(boolean isDisplay) {
        setDisplayLoading(isDisplay, null);
    }

    public void setDisplayLoading(boolean isDisplay, final Cancelable cancelable) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext, getString(R.string.Hint_Loading));
        }
        if (isDisplay) {
            if (cancelable != null) {
                mProgressDialog.setCancelable(true);
                mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        cancelable.cancel();
                    }
                });
            } else {
                mProgressDialog.setCancelable(false);
                mProgressDialog.setOnCancelListener(null);
            }
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        } else {
            mProgressDialog.setOnCancelListener(null);
            mProgressDialog.dismiss();
        }
    }

    //显示一个content内容的对话框，这个方法其实是Contract.View的方法，这里先实现一个默认的方法，每个子类就不需要单独实现它，详情见BaseView
    public void showDialog(String content) {
        showDialog(content, null);
    }

    public void showDialog(String content, DialogInterface.OnDismissListener listener) {
        new AlertDialog(mContext)
                .setMessage(content)
                .setOnDismissListener(listener)
                .show();
    }

    //显示一个Error错误，默认使用toast实现的，这个方法其实是Contract.View的方法，这里先实现一个默认的方法，每个子类就不需要单独实现它，详情见BaseView
    public void showError(String error) {
        if (TextUtils.isEmpty(error)) {
            return;
        }
        showShortToast(error,SaltyToast.TYPE_ERROR);
        vibrate(50);
    }

    //判断mPresenter是否和Contract.View绑定了，这个方法其实是Contract.View的方法，这里先实现一个默认的方法，每个子类就不需要单独实现它，详情见BaseView
    public boolean isAttachedToPresenter() {
        return mPresenter != null;
    }

    @SuppressWarnings("unchecked")
    private void initPresenter() {
        if (!(this instanceof BasicView)) {
            return;
        }
        BasicView view = (BasicView) this;
        Class thisClass = this.getClass();
        while (thisClass.getSuperclass() != null) {
            Class superClass = thisClass.getSuperclass();
            if (superClass != BasicCompatActivity.class) {
                thisClass = superClass;
                continue;
            }

            Type type = thisClass.getGenericSuperclass();
            if (!(type instanceof ParameterizedType)) {
                return;
            }

            ParameterizedType pType = (ParameterizedType) type;
            for (Type genericType : pType.getActualTypeArguments()) {
                if (!(genericType instanceof Class)) {
                    continue;
                }
                Class pClass = (Class) genericType;
                try {
                    mPresenter = (P) pClass.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("初始化Presenter失败,class:" + pClass.getName(), e);
                }

                try {
                    mPresenter.attachView(view);
                } catch (ClassCastException e) {
                    throw new RuntimeException("初始化Presenter失败,class:", e);
                }
            }
            return;
        }
    }
}
