package me.zhixingye.base.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import me.zhixingye.base.R;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class SaltyToast {
    public static final int TYPE_HINT = 1;
    public static final int TYPE_ERROR = 2;
    public static final int TYPE_SUCCESS = 3;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_HINT, TYPE_ERROR, TYPE_SUCCESS})
    public @interface ToastType {
    }

    public static void showLongToast(Context ctx, String context, @ToastType int toastType) {
        showToast(ctx, context, toastType, Toast.LENGTH_LONG);
    }

    public static void showShortToast(Context ctx, String context, @ToastType int toastType) {
        showToast(ctx, context, toastType, Toast.LENGTH_SHORT);
    }

    @SuppressLint("InflateParams")
    public static void showToast(Context ctx, String content, @ToastType int toastType, int duration) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.view_toast, null, false);
        ImageView ivIcon = v.findViewById(R.id.mIvIcon);
        TextView tvContent = v.findViewById(R.id.mTvContent);

        tvContent.setText(content);
        switch (toastType) {
            case TYPE_ERROR:
                ivIcon.setImageResource(R.drawable.ic_toast_error);
                break;
            case TYPE_SUCCESS:
                ivIcon.setImageResource(R.drawable.ic_toast_success);
                break;
            case TYPE_HINT:
                ivIcon.setImageResource(R.drawable.ic_toast_hint);
                break;
        }

        Toast toast = new Toast(ctx);
        toast.setView(v);
        toast.setGravity(
                Gravity.CENTER_HORIZONTAL | Gravity.TOP,
                0, (int) (getScreenHeight(ctx)*0.55));
        toast.setDuration(duration);
        toast.show();
    }

    private static int getScreenHeight(Context ctx) {
        WindowManager manager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        Display display = manager.getDefaultDisplay();
        display.getRealMetrics(dm);
        return dm.heightPixels;
    }
}
