package me.zhixingye.base.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import me.zhixingye.base.component.BasicDialogFragment;
import me.zhixingye.base.R;


/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class ProgressDialogFragment extends BasicDialogFragment {

    private static final String EXTRA_KEY_HINT_TEXT = "hintText";
    private static final String EXTRA_KEY_IS_CANCELABLE = "isCancelable";

    public static ProgressDialogFragment create(String hintText, boolean isCancelable) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_KEY_HINT_TEXT, hintText);
        bundle.putBoolean(EXTRA_KEY_IS_CANCELABLE, isCancelable);
        ProgressDialogFragment fragment = new ProgressDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private TextView mTvHint;

    @Override
    protected int getLayoutID() {
        return R.layout.dialog_progress;
    }

    @Override
    protected void init(View rootView) {
        mTvHint = rootView.findViewById(R.id.ProgressDialog_mTvHint);
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        String hintText = getHintText();
        if (hintText != null) {
            mTvHint.setText(hintText);
        }
    }

    @Override
    protected void setupDialog(@NonNull Dialog dialog) {
        dialog.setCancelable(getCancelable());
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.bg_progress_dialog));
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

            int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 96f, getResources().getDisplayMetrics());
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = size;
            lp.height = size;
            lp.gravity = Gravity.CENTER; // 紧贴底部
            lp.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING;
            window.setAttributes(lp);
        }
    }

    private String getHintText() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            return bundle.getString(EXTRA_KEY_HINT_TEXT);
        }
        return null;
    }

    private boolean getCancelable() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            return bundle.getBoolean(EXTRA_KEY_IS_CANCELABLE);
        }
        return false;
    }

}
