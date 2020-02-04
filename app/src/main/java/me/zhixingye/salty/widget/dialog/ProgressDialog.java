package me.zhixingye.salty.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import me.zhixingye.salty.R;
import me.zhixingye.salty.util.AndroidHelper;


/**
 * Created by YZX on 2018年02月12日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */


public class ProgressDialog extends Dialog {

    private TextView mTvHint;
    private Context mContext;

    private CharSequence mHint;

    public ProgressDialog(@NonNull Context context, CharSequence hintText) {
        this(context, hintText, false);
    }

    public ProgressDialog(@NonNull Context context, CharSequence hintText, boolean isCancelable) {
        super(context);
        mContext = context;
        mHint = hintText;
        setCancelable(isCancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_progress);
        mTvHint = findViewById(R.id.ProgressDialog_mTvHint);
        mTvHint.setText(mHint);
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.bg_progress_dialog));
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.CENTER; // 紧贴底部
            lp.width = (int) AndroidHelper.dip2px(96);
            lp.height = (int) AndroidHelper.dip2px(96);
            lp.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING;
            window.setAttributes(lp);
        }
    }

    public void setHintText(CharSequence hintText) {
        mTvHint.setText(hintText);
    }

    @Override
    public void show() {
        if (isShowing()) {
            return;
        }
        this.show(-1);
    }

    public void show(int systemUiVisibility) {
        if (systemUiVisibility != -1) {
            Window window = getWindow();
            if (window != null) {
                View decor = window.getDecorView();
                decor.setSystemUiVisibility(systemUiVisibility);
            }

        }
        if (!isShowing()) {
            super.show();
        }
    }


    public void show(String hintText) {
        mTvHint.setText(hintText);
        show();
    }
}
