package me.zhixingye.salty.widget.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.Nullable;
import me.zhixingye.salty.R;

/**
 * Created by zhixingye on 2020年03月01日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class PhoneEditText extends LinearLayout {

    private TextInputLayout mTilPhonePrefix;
    private TextInputLayout mTilPhoneSuffix;
    private EditText mEtPhonePrefix;
    private EditText mEtPhoneSuffix;

    public PhoneEditText(Context context) {
        this(context, null);
    }

    public PhoneEditText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhoneEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setOrientation(HORIZONTAL);

        LayoutInflater.from(getContext()).inflate(R.layout.view_phone_edit_text, this, true);

        mTilPhonePrefix = findViewById(R.id.mTilPhonePrefix);
        mEtPhonePrefix = findViewById(R.id.mEtPhonePrefix);
        mTilPhoneSuffix = findViewById(R.id.mTilPhoneSuffix);
        mEtPhoneSuffix = findViewById(R.id.mEtPhoneSuffix);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mTilPhonePrefix.setEnabled(enabled);
        mEtPhonePrefix.setEnabled(enabled);
        mTilPhoneSuffix.setEnabled(enabled);
        mEtPhoneSuffix.setEnabled(enabled);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        throw new RuntimeException("该view不支持设置OnClickListener");
    }

    public String getPhonePrefixText() {
        CharSequence callPrefix = mEtPhonePrefix.getText();
        if (TextUtils.isEmpty(callPrefix)) {
            return "";
        }
        return callPrefix.toString();
    }

    public String getPhoneSuffixText(){
        CharSequence callPrefix = mEtPhoneSuffix.getText();
        if (TextUtils.isEmpty(callPrefix)) {
            return "";
        }
        return callPrefix.toString();
    }

    public void setPhonePrefixText(CharSequence text) {
        mEtPhonePrefix.setText(text);
    }

    public void setPhoneSuffixText(CharSequence text){
        mEtPhoneSuffix.setText(text);
    }

    public void setError(String error) {
        mTilPhoneSuffix.setError(error);
    }
}
