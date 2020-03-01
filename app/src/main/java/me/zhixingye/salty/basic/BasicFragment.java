package me.zhixingye.salty.basic;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Gravity;
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

/**
 * Created by YZX on 2017年06月12日.
 * 生命太短暂,不要去做一些根本没有人想要的东西
 */


public abstract class BasicFragment<P extends BasicPresenter> extends Fragment {

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

    public void showToast(String content) {
        showToast(content, Toast.LENGTH_SHORT);
    }

    public void showLongToast(String content) {
        showToast(content, Toast.LENGTH_LONG);
    }

    public void showToast(String content, int duration) {
        Toast toast = Toast.makeText(mContext, content, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void setEnableLoading(boolean isEnable) {
        setEnableLoading(isEnable, null);
    }

    public void setEnableLoading(boolean isEnable, final Cancelable cancelable) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext, getString(R.string.Hint_Loading));
        }
        if (isEnable) {
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
        showToast(error);
        vibrate(50);
    }

    //判断mPresenter是否和Contract.View绑定了，这个方法其实是Contract.View的方法，这里先实现一个默认的方法，每个子类就不需要单独实现它，详情见BaseView
    public boolean isAttachedToPresenter() {
        return mPresenter != null;
    }

    //初始化Presenter，这里用到了反射，这段不是很好理解，可以断点Debug一行行看代码执行情况以及对应的一些class变量
    @SuppressWarnings("unchecked")
    private void initPresenter() {
        if (this instanceof BasicView) {
            BasicView view = (BasicView) this;
            mPresenter = (P) view.getPresenter();
            if (mPresenter == null) {
                return;
            }
            Class aClass = this.getClass();//获取一个Presenter，子类实现了getPresenter();
            while (aClass != null) {
                //获取父类，这里的type就是BaseActivity
                Type type = aClass.getGenericSuperclass();
                if (type instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    //获取泛型类型，这里的genericType就是泛型T
                    Type genericType = parameterizedType.getActualTypeArguments()[0];
                    //这里获取Presenter实现的那些接口
                    Class<?>[] interfaces = mPresenter.getClass().getInterfaces();
                    //判断Presenter实现的接口是否有泛型T，本质上就是判断mPresenter是否就是泛型T的实例
                    for (Class c : interfaces) {
                        if (c == genericType) {
                            mPresenter.attachView(view);
                            return;
                        }
                    }
                } else {
                    aClass = aClass.getSuperclass();
                }
            }
            mPresenter = null;
        }
    }
}
