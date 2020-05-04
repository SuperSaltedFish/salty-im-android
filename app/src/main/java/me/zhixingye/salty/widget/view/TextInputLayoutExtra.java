package me.zhixingye.salty.widget.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月04日.
 */
public class TextInputLayoutExtra extends TextInputLayout implements TextWatcher {
    public TextInputLayoutExtra(@NonNull Context context) {
        super(context);
    }

    public TextInputLayoutExtra(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextInputLayoutExtra(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(@NonNull View child, int index, @NonNull ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (child instanceof EditText) {
            ((EditText) child).removeTextChangedListener(this);
            ((EditText) child).addTextChangedListener(this);
        }
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        boolean result = super.addViewInLayout(child, index, params, preventRequestLayout);
        if (child instanceof EditText) {
            ((EditText) child).removeTextChangedListener(this);
            ((EditText) child).addTextChangedListener(this);
        }
        return result;
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        setError("");
    }
}
