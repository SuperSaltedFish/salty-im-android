package me.zhixingye.salty.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import androidx.core.view.ViewCompat;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */

public class AnimationUtil {

    public static void rotateAnim(View view, float degree, int millisecond) {
        Animation anim = new RotateAnimation(0f, degree, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true);
        anim.setDuration(millisecond);
        view.startAnimation(anim);
    }

    public static void scaleAnim(View view, float toScaleX, float toScaleY, int millisecond) {
        ViewCompat.animate(view)
                .scaleX(toScaleX)
                .scaleY(toScaleY)
                .setDuration(millisecond);
    }

    public static void shakeAnim(View view) {
        TranslateAnimation animation = new TranslateAnimation(-8, 8, 0, 0);
        animation.setDuration(20);
        animation.setRepeatCount(4);
        animation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(animation);
    }

    public static void circularRevealHideAnim(View view,
            Animator.AnimatorListener... animListener) {
        int width = view.getWidth();
        int height = view.getHeight();
        int radius = Math.max(width, height);
        Animator animator = ViewAnimationUtils
                .createCircularReveal(view, width / 2, height / 2, radius / 2f, 0);
        if (animListener != null) {
            for (Animator.AnimatorListener listener : animListener) {
                if (listener != null) {
                    animator.addListener(listener);
                }
            }
        }
        animator.start();
    }

    public static void circularRevealShowAnim(View view,
            Animator.AnimatorListener... animListener) {
        int width = view.getWidth();
        int height = view.getHeight();
        int radius = Math.max(width, height);
        Animator animator = ViewAnimationUtils
                .createCircularReveal(view, width / 2, height / 2, 0, radius / 2f);
        if (animListener != null) {
            for (Animator.AnimatorListener listener : animListener) {
                if (listener != null) {
                    animator.addListener(listener);
                }
            }
        }
        animator.start();
    }

    public static void circularRevealShowByFullActivityAnim(
            Activity activity,
            View triggerView,
            Drawable image,
            final Animator.AnimatorListener animListener) {
        int[] location = new int[2];
        triggerView.getLocationInWindow(location);
        final int cx = location[0] + triggerView.getWidth() / 2;
        final int cy = location[1] + triggerView.getHeight() / 2;
        final ImageView view = new ImageView(activity);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setImageDrawable(image);
        final ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        int w = decorView.getWidth();
        int h = decorView.getHeight();
        decorView.addView(view, w, h);

        int maxW = Math.max(cx, w - cx);
        int maxH = Math.max(cy, h - cy);
        final int finalRadius = (int) Math.sqrt(maxW * maxW + maxH * maxH) + 1;
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        int maxRadius = (int) Math.sqrt(w * w + h * h) + 1;
        double rate = 1d * finalRadius / maxRadius;
        final long finalDuration = (long) (618 * Math.sqrt(rate));
        anim.setDuration((long) (finalDuration * 0.9));
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animation) {
                if (animListener != null) {
                    animListener.onAnimationCancel(animation);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if (animListener != null) {
                    animListener.onAnimationRepeat(animation);
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                if (animListener != null) {
                    animListener.onAnimationStart(animation);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animation.removeListener(this);
                if (animListener != null) {
                    animListener.onAnimationEnd(animation);
                }
            }
        });
        anim.start();
    }

    public static void circularRevealHideByFullActivityAnim(Activity activity, Drawable image) {
        WindowManager manager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        if (manager == null) {
            return;
        }
        DisplayMetrics dm = new DisplayMetrics();
        Display display = manager.getDefaultDisplay();
        display.getRealMetrics(dm);
        final int width = dm.widthPixels;
        final int height = dm.heightPixels;
        final int cx = width / 2;
        final int cy = height / 2;

        final ImageView view = new ImageView(activity);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setImageDrawable(image);

        final ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        decorView.addView(view,
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        int maxW = Math.max(cx, width - cx);
        int maxH = Math.max(cy, height - cy);
        final int startRadius = (int) Math.sqrt(maxW * maxW + maxH * maxH) + 1;
        final int endRadius = 0;

        view.post(new Runnable() {
            @Override
            public void run() {
                Animator anim = ViewAnimationUtils
                        .createCircularReveal(view, cx, cy, startRadius, endRadius);
                int maxRadius = (int) Math.sqrt(width * width + height * height) + 1;
                double rate = 1d * startRadius / maxRadius;
                long duration = (long) (618 * Math.sqrt(rate));
                anim.setDuration((long) (duration * 0.9));
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setImageDrawable(null);
                        decorView.removeView(view);
                        animation.removeAllListeners();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        onAnimationEnd(animation);
                    }

                    @Override
                    public void onAnimationPause(Animator animation) {
                        onAnimationEnd(animation);
                    }
                });
                anim.start();
            }
        });
    }

}
