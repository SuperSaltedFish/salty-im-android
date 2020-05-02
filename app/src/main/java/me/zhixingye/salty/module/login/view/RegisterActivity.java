package me.zhixingye.salty.module.login.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import me.zhixingye.salty.R;
import me.zhixingye.salty.basic.BasicCompatActivity;
import me.zhixingye.salty.configure.AppConfig;
import me.zhixingye.salty.module.login.contract.RegisterContract;
import me.zhixingye.salty.module.login.presenter.RegisterPresenter;
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
public class RegisterActivity extends BasicCompatActivity<RegisterPresenter> implements RegisterContract.View {

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
    private TextView mTvAgreement;

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
        mTvAgreement = findViewById(R.id.mTvAgreement);
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        setSystemUiMode(SYSTEM_UI_MODE_TRANSPARENT_LIGHT_BAR_STATUS_AND_NAVIGATION);
        setDisplayHomeAsUpEnabled(true);

        setupPasswordRuleVerificationAnimation();
        setupUserAgreement();

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

    private void setupUserAgreement() {
        String userAgreement = "注册即代表您同意";
        String focus = "《咸鱼协议》";
        int startIndex = userAgreement.length();
        SpannableString sStr = new SpannableString(userAgreement + focus);
        sStr.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorAccent)) {
            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                super.updateDrawState(textPaint);
                textPaint.setFakeBoldText(true);
            }
        }, startIndex, startIndex + focus.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mTvAgreement.setText(sStr);
    }

    private void tryNext() {
        final String telephone = mPetPhone.getPhoneSuffixText();
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

        mPBtnNext.startHideAnim(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mPresenter.obtainTelephoneRegisterVerifyCode(telephone);
            }
        });
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

    @Override
    public void startPhoneVerifyActivity() {
        PhoneVerifyCodeActivity.startActivity(this);
    }

    @Override
    public void showError(String error) {
        super.showError(error);
        mPBtnNext.startShowAnim();
    }
}
