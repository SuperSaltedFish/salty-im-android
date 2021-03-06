package me.zhixingye.base.component;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import me.zhixingye.base.BuildConfig;
import me.zhixingye.base.R;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public abstract class BasicDialogFragment
        extends DialogFragment
        implements LifecycleUIComponent {

    @LayoutRes
    protected abstract int getLayoutID();

    protected abstract void init(View rootView);

    protected abstract void setup(Bundle savedInstanceState);

    protected abstract void setupDialog(@NonNull Dialog dialog);

    public Context mContext;

    private DialogInterface.OnDismissListener mOnDismissListener;

    private boolean isSingleInstance;//只能弹出一个本实例的对话框，会判断对话框是否已经存在

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutID(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setup(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Dialog dialog = getDialog();
        if (dialog != null) {
            setupDialog(dialog);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public int show(@NonNull FragmentTransaction transaction, @Nullable String tag) {
        try {
            return super.show(transaction, tag);
        } catch (Exception e) {
            //防止各种情况下可能发生的异常抛出
            if (BuildConfig.DEBUG) {
                throw e;
            }
            e.printStackTrace();
        }
        return -1;
    }

    //这里可以理解成单例模式去show，先判断UI上是否已经有该Tag,有就不继续显示
    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        if (isSingleInstance && !TextUtils.isEmpty(tag)) {
            if (manager.findFragmentByTag(tag) != null) {
                return;
            }
        }
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            //防止各种情况下可能发生的异常抛出
            if (BuildConfig.DEBUG) {
                throw e;
            }
            e.printStackTrace();
        }
    }

    //这里可以理解成单例模式去show，先判断UI上是否已经有该Tag,有就不继续显示
    @Override
    public void showNow(@NonNull FragmentManager manager, @Nullable String tag) {
        if (isSingleInstance && !TextUtils.isEmpty(tag)) {
            if (manager.findFragmentByTag(tag) != null) {
                return;
            }
        }
        try {
            super.showNow(manager, tag);
        } catch (Exception e) {
            //防止各种情况下可能发生的异常抛出
            if (BuildConfig.DEBUG) {
                throw e;
            }
            e.printStackTrace();
        }
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        mOnDismissListener = listener;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss(dialog);
        }
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

    protected void setWindowEnableDimBehind(boolean isEnableDimBehind) {
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

    protected void setWindowsDimAmount(@FloatRange(from = 0f, to = 1f) float amount) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setDimAmount(amount);
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

    protected void setBackgroundDrawableResource(@DrawableRes int resId) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawableResource(resId);
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

}
