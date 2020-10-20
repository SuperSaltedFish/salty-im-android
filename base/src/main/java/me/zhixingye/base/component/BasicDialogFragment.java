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
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import me.zhixingye.base.R;
import me.zhixingye.base.listener.CancelableListener;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public abstract class BasicDialogFragment
        extends DialogFragment
        implements UIComponent {

    @LayoutRes
    protected abstract int getLayoutID();

    protected abstract void init(View rootView);

    protected abstract void setup(Bundle savedInstanceState);

    protected abstract void setupDialog(@NonNull Dialog dialog);

    public Context mContext;

    private CancelableListener mCancelableListener;

    private boolean isSingleInstance;//只能弹出一个本实例的对话框，会判断对话框是否已经存在

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogFragmentStyle);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        setupDialog(dialog);
        return dialog;
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
        init(view);
        setup(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //默认从下方显示
        setWindowsGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        setWindowLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
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
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        if (mCancelableListener != null) {
            mCancelableListener.onCancel();
        }
    }

    @Override
    public int show(@NonNull FragmentTransaction transaction, @Nullable String tag) {
        try {
            return super.show(transaction, tag);
        } catch (Exception e) {
            //防止各种情况下可能发生的异常抛出
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
            e.printStackTrace();
        }
    }

    public void setCancelableListener(CancelableListener listener) {
        mCancelableListener = listener;
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

}
