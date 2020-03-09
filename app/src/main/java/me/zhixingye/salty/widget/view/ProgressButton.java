package me.zhixingye.salty.widget.view;

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
import me.zhixingye.salty.R;

/**
 * Created by YZX on 2019年03月14日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class ProgressButton extends ConstraintLayout {

    private Button mButton;
    private ProgressBar mProgressBar;
    private OnClickListener mOnClickListener;

    private TransparentDialog mTransparentDialog;

    private Animator mShowAnimator;
    private Animator mHideAnimator;

    private boolean isLayoutCompleted;

    public ProgressButton(@NonNull Context context) {
        this(context, null);
    }

    public ProgressButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
        if(!isInEditMode()){
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
            setEnabled( array.getBoolean(R.styleable.ProgressButton_enabled,true));
            array.recycle();
        }

        mButton.setOnClickListener(v -> {
            if (mOnClickListener != null) {
                mOnClickListener.onClick(ProgressButton.this);
            }
        });
        mButton.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> isLayoutCompleted = true);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mTransparentDialog.isShowing()) {
            mTransparentDialog.dismiss();
        }
        super.onDetachedFromWindow();
    }

    public void startShowAnim(@Nullable Animator.AnimatorListener listener) {
        if (mButton.getVisibility() == View.VISIBLE || (mShowAnimator != null && mShowAnimator.isRunning())) {
            return;
        }
        if (mShowAnimator != null) {
            mShowAnimator.cancel();
            mShowAnimator.removeAllListeners();
        }
        if (isLayoutCompleted) {
            new CreateShowAnimRunnable(listener).run();
        } else {
            post(new CreateShowAnimRunnable(listener));
        }
    }

    public void startHideAnim(@Nullable Animator.AnimatorListener listener) {
        if (mButton.getVisibility() == View.INVISIBLE || (mHideAnimator != null && mHideAnimator.isRunning())) {
            return;
        }
        if (mHideAnimator != null) {
            mHideAnimator.cancel();
            mHideAnimator.removeAllListeners();
        }
        if (isLayoutCompleted) {
            new CreateHintAnimRunnable(listener).run();
        } else {
            post(new CreateHintAnimRunnable(listener));
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

    private final class CreateShowAnimRunnable implements Runnable {
        private Animator.AnimatorListener mListener;

        private CreateShowAnimRunnable(Animator.AnimatorListener listener) {
            mListener = listener;
        }

        @Override
        public void run() {
            mShowAnimator = circularRevealShowAnim(mButton);
            mShowAnimator.addListener(mShowListener);
            if (mListener != null) {
                mShowAnimator.addListener(mListener);
            }
            mShowAnimator.start();
        }
    }

    private final class CreateHintAnimRunnable implements Runnable {
        private Animator.AnimatorListener mListener;

        private CreateHintAnimRunnable(Animator.AnimatorListener listener) {
            mListener = listener;
        }

        @Override
        public void run() {
            mHideAnimator = circularRevealHideAnim(mButton);
            mHideAnimator.addListener(mHideListener);
            if (mListener != null) {
                mHideAnimator.addListener(mListener);
            }
            mHideAnimator.start();
            clearCurrentFocus();
        }
    }


    private final Animator.AnimatorListener mShowListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationStart(Animator animation) {
            mButton.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mButton.setClickable(true);
            mProgressBar.setVisibility(View.INVISIBLE);
            mTransparentDialog.dismiss();
        }
    };

    private final Animator.AnimatorListener mHideListener = new AnimatorListenerAdapter() {
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
        }
    };


    public void setText(CharSequence text) {
        mButton.setText(text);
    }

    @Override
    public void setEnabled(boolean enabled) {
        recursiveSetEnabled(this,enabled);
        super.setEnabled(enabled);
    }

    @Override
    public void setClickable(boolean clickable) {
        recursiveSetClickable(this,clickable);
        super.setClickable(clickable);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener listener) {
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

    private static Animator circularRevealHideAnim(View view) {
        int width = view.getWidth();
        int height = view.getHeight();
        int diameter = Math.max(width, height);
        return ViewAnimationUtils.createCircularReveal(view, width / 2, height / 2, diameter / 2f, 0);

    }

    private static Animator circularRevealShowAnim(View view) {
        int width = view.getWidth();
        int height = view.getHeight();
        int diameter = Math.max(width, height);
        return ViewAnimationUtils.createCircularReveal(view, width / 2, height / 2, 0, diameter / 2f);
    }

    private static final class TransparentDialog extends Dialog {//这个对话框的的作用：当执行动画或ProgressBar可见时弹出全透明的dialog，让返回按钮和UI上的其他东西不可用

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
