package me.zhixingye.salty.basic;

import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
public abstract class BasicDialogFragment<P extends BasicPresenter> extends DialogFragment {

    private static final String TAG = "BasicFragment";

    @LayoutRes
    protected abstract int getLayoutID();

    protected abstract void init(View parentView);

    protected abstract void setup(Bundle savedInstanceState);

    protected P mPresenter;

    public Context mContext;

    protected ProgressDialog mProgressDialog;

    private Vibrator mVibrator;

    private boolean isSingleInstance = true;//只能弹出一个本实例的对话框，会判断对话框是否已经存在

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        mVibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogFragmentStyle);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //默认从下方显示
        setWindowsGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        setWindowLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //这里new一个FrameLayout只是用来inflate中的参数，不然layout中得参数无法解析
        if (container == null) {
            container = new FrameLayout(mContext);
        }
        return inflater.inflate(getLayoutID(), container, false);
    }

    @Override
    public final void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPresenter();
        init(view);
        setup(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        if (mProgressDialog != null) {
            mProgressDialog.setOnCancelListener(null);
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }


    //这里可以理解成单例模式去show，先判断UI上是否已经有该Tag,有就不继续显示
    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        if (isSingleInstance && !TextUtils.isEmpty(tag)) {
            if (manager.findFragmentByTag(tag) != null) {
                return;
            }
        }
        super.show(manager, tag);
    }

    //这里可以理解成单例模式去show，先判断UI上是否已经有该Tag,有就不继续显示
    @Override
    public void showNow(@NonNull FragmentManager manager, @Nullable String tag) {
        if (isSingleInstance && !TextUtils.isEmpty(tag)) {
            if (manager.findFragmentByTag(tag) != null) {
                return;
            }
        }
        super.showNow(manager, tag);
    }

    //是否对同样的dialogFragment进行单例的判断
    public void setSingleInstance(boolean singleInstance) {
        isSingleInstance = singleInstance;
    }

    //获取父Fragment的高度，有需求说子Fragment的高度要和父一样高，比如收银台
    protected int getParentFragmentHeight() {
        Fragment fragment = getParentFragment();
        if (fragment != null) {
            View view = fragment.getView();
            if (view != null) {
                return view.getHeight();
            }
        }
        return 0;
    }

    //设置自动设置高度，高度为父Fragment的高度
    protected boolean setupHeightFromParentFragment() {
        Fragment fragment = getParentFragment();
        if (fragment != null) {
            View view = fragment.getView();
            if (view != null && view.getHeight() > 0) {
                setWindowLayout(WindowManager.LayoutParams.MATCH_PARENT, view.getHeight());
                return true;
            }
        }
        return false;
    }

    protected void setWindowLayout(int width, int height) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setLayout(width, height);
            }
        }
    }

    protected void setWindowAnimations(@StyleRes int res) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setWindowAnimations(res);
            }
        }
    }

    protected void setEnableWindowDimBehind(boolean isEnableDimBehind) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                if (isEnableDimBehind) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                } else {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                }
            }
        }
    }

    protected void setWindowsBackgroundDrawable(Drawable drawable) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(drawable);
            }
        }
    }

    protected void setWindowsGravity(int gravity) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setGravity(gravity);
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

    //是否显示loading对话框，这个方法其实是Contract.View的方法，这里先实现一个默认的方法，每个子类就不需要单独实现它，详情见BaseView
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
        if (TextUtils.isEmpty(error)) {
            return;
        }
        showShortToast(error,SaltyToast.TYPE_ERROR);
        vibrate(50);
    }

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
