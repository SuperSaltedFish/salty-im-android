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

    private TextInputLayout mTilCallPrefix;
    private TextInputLayout mTilTelephone;
    private EditText mEtCallPrefix;
    private EditText mEtTelephone;

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

        mTilCallPrefix = findViewById(R.id.mTilCallPrefix);
        mEtCallPrefix = findViewById(R.id.mEtCallPrefix);
        mTilTelephone = findViewById(R.id.mTilTelephone);
        mEtTelephone = findViewById(R.id.mEtTelephone);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mTilCallPrefix.setEnabled(enabled);
        mEtCallPrefix.setEnabled(enabled);
        mTilTelephone.setEnabled(enabled);
        mEtTelephone.setEnabled(enabled);
    }

    public void setCallPrefixText(String text) {
        mEtCallPrefix.setText(text);
    }

    public String getCallPrefixText() {
        CharSequence callPrefix = mEtCallPrefix.getText();
        if (TextUtils.isEmpty(callPrefix)) {
            return "";
        }
        return callPrefix.toString();
    }

    public void setTelephoneText(CharSequence text) {
        mEtTelephone.setText(text);
    }

    public String getTelephoneText() {
        CharSequence callPrefix = mEtCallPrefix.getText();
        if (TextUtils.isEmpty(callPrefix)) {
            return "";
        }
        return callPrefix.toString();
    }

    public void setTelephoneHintText(CharSequence text) {
        mTilTelephone.setHelperText(text);
    }

    public void setTelephoneErrorText(CharSequence text) {
        mTilTelephone.setError(text);
    }
}
