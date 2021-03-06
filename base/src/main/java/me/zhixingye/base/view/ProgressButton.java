package me.zhixingye.base.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import me.zhixingye.base.R;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class ProgressButton extends ConstraintLayout {

    private Button mButton;
    private ProgressBar mProgressBar;
    private View.OnClickListener mOnClickListener;

    private TransparentDialog mTransparentDialog;

    private Animator mShowAnimator;
    private Animator mHideAnimator;

    public ProgressButton(@NonNull Context context) {
        this(context, null);
    }

    public ProgressButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
        if (!isInEditMode()) {
            mTransparentDialog = new TransparentDialog(context);
        }
    }

    private void initView(Context context, @Nullable AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_progress_button, this, true);
        mButton = view.findViewById(R.id.mButton);
        mProgressBar = view.findViewById(R.id.mProgressBar);

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ProgressButton);
            setText(array.getString(R.styleable.ProgressButton_text));
            mButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, array.getDimension(R.styleable.ProgressButton_textSize, mButton.getTextSize()));
            setEnabled(array.getBoolean(R.styleable.ProgressButton_enabled, true));

            int buttonPaddingStart = array.getDimensionPixelSize(R.styleable.ProgressButton_buttonPaddingStart, mButton.getPaddingStart());
            int buttonPaddingEnd = array.getDimensionPixelSize(R.styleable.ProgressButton_buttonPaddingEnd, mButton.getPaddingEnd());
            int buttonPaddingTop = array.getDimensionPixelSize(R.styleable.ProgressButton_buttonPaddingTop, mButton.getPaddingTop());
            int buttonPaddingBottom = array.getDimensionPixelSize(R.styleable.ProgressButton_buttonPaddingBottom, mButton.getPaddingBottom());
            mButton.setPadding(buttonPaddingStart, buttonPaddingTop, buttonPaddingEnd, buttonPaddingBottom);

            mButton.setMinWidth(array.getDimensionPixelSize(R.styleable.ProgressButton_buttonMinWidth, mButton.getMinWidth()));
            mButton.setMinHeight(array.getDimensionPixelSize(R.styleable.ProgressButton_buttonMinHeight, mButton.getMinHeight()));
            array.recycle();
        }

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.onClick(ProgressButton.this);
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mTransparentDialog.isShowing()) {
            mTransparentDialog.dismiss();
        }
        super.onDetachedFromWindow();
    }

    public void startShowAnim() {
        startShowAnim(null);
    }

    public void startShowAnim(@Nullable Animator.AnimatorListener listener) {
        if (mButton.getVisibility() == View.VISIBLE) {
            if (listener != null) {
                listener.onAnimationEnd(null);
            }
            return;
        }
        createShowAnimIfNeed();
        if (listener != null) {
            mShowAnimator.addListener(listener);
        }
        cancelHideAnim();
        removeCallbacks(mShowAnimRunnable);
        post(mShowAnimRunnable);
    }

    public void startHideAnim() {
        startHideAnim(null);
    }

    public void startHideAnim(@Nullable Animator.AnimatorListener listener) {
        if (mButton.getVisibility() == View.INVISIBLE) {
            if (listener != null) {
                listener.onAnimationEnd(null);
            }
            return;
        }
        createHideAnimIfNeed();
        if (mHideAnimator != null) {
            mHideAnimator.addListener(listener);
        }
        cancelShowAnim();
        removeCallbacks(mHintAnimRunnable);
        post(mHintAnimRunnable);
    }

    private void createShowAnimIfNeed() {
        if (mShowAnimator != null) {
            return;
        }
        mShowAnimator = createShowCircularReveal(mButton);
        mShowAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mButton.setClickable(true);
                mProgressBar.setVisibility(View.INVISIBLE);
                mTransparentDialog.dismiss();
                cancelShowAnim();
            }
        });
    }

    private void createHideAnimIfNeed() {
        if (mHideAnimator != null) {
            return;
        }
        mHideAnimator = createHideCircularReveal(mButton);
        mHideAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mButton.setClickable(false);
                mProgressBar.setVisibility(View.VISIBLE);
                if (!mTransparentDialog.isShowing()) {
                    mTransparentDialog.show();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mButton.setVisibility(View.INVISIBLE);
                cancelHideAnim();
            }
        });
    }

    private void cancelShowAnim() {
        if (mShowAnimator != null) {
            mShowAnimator.cancel();
            mShowAnimator = null;
        }
    }

    private void cancelHideAnim() {
        if (mHideAnimator != null) {
            mHideAnimator.cancel();
            mHideAnimator = null;
        }
    }

    private void clearCurrentFocus() {
        Context context = getContext();
        if (context instanceof Activity) {
            Window window = ((Activity) context).getWindow();
            if (window != null) {
                View focus = window.getCurrentFocus();
                if (focus != null) {
                    focus.clearFocus();
                    ViewParent parent = focus.getParent();
                    if (parent instanceof View) {
                        ((View) parent).setFocusable(true);
                        ((View) parent).setFocusableInTouchMode(true);
                        ((View) parent).requestFocus();
                    }
                }
            }
        }
    }

    private final Runnable mShowAnimRunnable = new Runnable() {
        @Override
        public void run() {
            mShowAnimator.start();
        }
    };

    private final Runnable mHintAnimRunnable = new Runnable() {
        @Override
        public void run() {
            mHideAnimator.start();
            clearCurrentFocus();
        }
    };

    public void setText(CharSequence text) {
        mButton.setText(text);
    }

    @Override
    public void setEnabled(boolean enabled) {
        recursiveSetEnabled(this, enabled);
        super.setEnabled(enabled);
    }

    @Override
    public void setClickable(boolean clickable) {
        recursiveSetClickable(this, clickable);
        super.setClickable(clickable);
    }

    @Override
    public void setOnClickListener(@Nullable View.OnClickListener listener) {
        mOnClickListener = listener;
    }

    public Button getButton() {
        return mButton;
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    private static void recursiveSetEnabled(@NonNull final ViewGroup vg, final boolean enabled) {
        for (int i = 0, count = vg.getChildCount(); i < count; i++) {
            final View child = vg.getChildAt(i);
            child.setEnabled(enabled);
            if (child instanceof ViewGroup) {
                recursiveSetEnabled((ViewGroup) child, enabled);
            }
        }
    }

    private static void recursiveSetClickable(@NonNull final ViewGroup vg, final boolean clickable) {
        for (int i = 0, count = vg.getChildCount(); i < count; i++) {
            final View child = vg.getChildAt(i);
            child.setClickable(clickable);
            if (child instanceof ViewGroup) {
                recursiveSetEnabled((ViewGroup) child, clickable);
            }
        }
    }

    private static Animator createHideCircularReveal(View view) {
        int width = view.getWidth();
        int height = view.getHeight();
        int diameter = Math.max(width, height);
        return ViewAnimationUtils.createCircularReveal(view, width / 2, height / 2, diameter / 2f, 0);

    }

    private static Animator createShowCircularReveal(View view) {
        int width = view.getWidth();
        int height = view.getHeight();
        int diameter = Math.max(width, height);
        return ViewAnimationUtils.createCircularReveal(view, width / 2, height / 2, 0, diameter / 2f);
    }

    /**
     * 这个对话框的的作用：当执行动画或ProgressBar可见时弹出全透明的dialog，让返回按钮和UI上的其他东西不可用
     */
    private static final class TransparentDialog extends Dialog {

        TransparentDialog(@NonNull Context context) {
            super(context);
            setCanceledOnTouchOutside(false);
            setCancelable(false);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            Window window = getWindow();
            if (window != null) {
                window.setBackgroundDrawable(null);
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        }

    }
}
