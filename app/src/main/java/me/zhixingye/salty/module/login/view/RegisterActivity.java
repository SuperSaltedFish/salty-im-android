package me.zhixingye.salty.module.login.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import me.zhixingye.salty.R;
import me.zhixingye.salty.basic.BasicCompatActivity;
import me.zhixingye.salty.configure.AppConfig;
import me.zhixingye.salty.util.AnimationUtil;
import me.zhixingye.salty.util.RegexUtil;
import me.zhixingye.salty.widget.listener.SimpleTextWatcher;
import me.zhixingye.salty.widget.view.PhoneEditText;
import me.zhixingye.salty.widget.view.ProgressButton;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class RegisterActivity extends BasicCompatActivity {

    private static final String TAG = "RegisterActivity";

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, RegisterActivity.class));
    }

    private PhoneEditText mPetPhone;
    private EditText mEtPassword;
    private TextView mTvRuleCombination;
    private TextView mTvRuleRepeatedNumbers;
    private TextView mTvRuleLength;
    private EditText mEtConfirmPassword;
    private TextView mTvRuleConsistency;
    private ProgressButton mPBtnNext;
    private TextView mTvTermRules;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_register;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mPetPhone = findViewById(R.id.mPetPhone);
        mEtPassword = findViewById(R.id.mEtPassword);
        mTvRuleCombination = findViewById(R.id.mTvRuleCombination);
        mTvRuleRepeatedNumbers = findViewById(R.id.mTvRuleRepeatedNumbers);
        mTvRuleLength = findViewById(R.id.mTvRuleLength);
        mEtConfirmPassword = findViewById(R.id.mEtConfirmPassword);
        mTvRuleConsistency = findViewById(R.id.mTvRuleConsistency);
        mPBtnNext = findViewById(R.id.mPBtnNext);
        mTvTermRules = findViewById(R.id.mTvTermRules);
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        setSystemUiMode(SYSTEM_UI_MODE_TRANSPARENT_LIGHT_BAR_STATUS_AND_NAVIGATION);
        setDisplayHomeAsUpEnabled(true);

        setupPasswordRuleVerificationAnimation();

        mPBtnNext.setOnClickListener(mOnClickListener);
    }

    private void setupPasswordRuleVerificationAnimation() {
        mEtPassword.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mTvRuleLength.setEnabled(
                        !TextUtils.isEmpty(s)
                                && (s.length() >= AppConfig.MIN_TELEPHONE_LENGTH));
                mTvRuleRepeatedNumbers.setEnabled(
                        !TextUtils.isEmpty(s)
                                && s.length() > 3
                                && !RegexUtil.isRepeatedNumber(s, 3));
                mTvRuleCombination.setEnabled(
                        !TextUtils.isEmpty(s)
                                && RegexUtil.isContainsNumbersOrLettersOrSymbol(s));
                mTvRuleConsistency.setEnabled(TextUtils.equals(s, mEtConfirmPassword.getText()));
            }
        });

        mEtConfirmPassword.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mTvRuleConsistency.setEnabled(TextUtils.equals(s, mEtPassword.getText()));
            }
        });
    }

    private void tryNext() {
        String telephone = mPetPhone.getPhoneSuffixText();
        if (TextUtils.isEmpty(telephone)) {
            mPetPhone.setError("请输入一个合法的手机号码");
            return;
        }
        if (!mTvRuleLength.isEnabled()) {
            AnimationUtil.shakeAnim(mTvRuleLength);
            return;
        }
        if (!mTvRuleRepeatedNumbers.isEnabled()) {
            AnimationUtil.shakeAnim(mTvRuleRepeatedNumbers);
            return;
        }
        if (!mTvRuleCombination.isEnabled()) {
            AnimationUtil.shakeAnim(mTvRuleCombination);
            return;
        }
        if (!mTvRuleConsistency.isEnabled()) {
            AnimationUtil.shakeAnim(mTvRuleConsistency);
            return;
        }
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mPBtnNext:
                    tryNext();
                    break;
            }
        }
    };

}
