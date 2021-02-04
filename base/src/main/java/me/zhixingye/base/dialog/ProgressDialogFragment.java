package me.zhixingye.base.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

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
        mTvHint = rootView.findViewById(R.id.mTvHint);
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        String hintText = getHintText();
        if (!TextUtils.isEmpty(hintText)) {
            mTvHint.setText(hintText);
            mTvHint.setVisibility(View.VISIBLE);
        } else {
            mTvHint.setVisibility(View.GONE);
        }
    }

    @Override
    protected void setupDialog(@NonNull Dialog dialog) {
        dialog.setCanceledOnTouchOutside(getCancelable());
        setCancelable(getCancelable());


        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 96, getResources().getDisplayMetrics());
        setWindowLayout(size, size);
        setBackgroundDrawableResource(R.drawable.bg_progress_dialog);
        setWindowsDimAmount(0.3f);
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
