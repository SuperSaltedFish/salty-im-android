package me.zhixingye.salty.module.login.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import me.zhixingye.base.component.BasicActivity;
import me.zhixingye.base.component.mvp.MVPBasicActivity;
import me.zhixingye.base.listener.SimpleTextWatcher;
import me.zhixingye.base.view.ProgressButton;
import me.zhixingye.salty.R;
import me.zhixingye.salty.configure.AppConfig;
import me.zhixingye.salty.module.login.contract.ResetLoginPasswordContract;
import me.zhixingye.salty.module.login.presenter.ResetLoginPasswordPresenter;
import me.zhixingye.salty.util.AnimationUtil;
import me.zhixingye.salty.util.RegexUtil;

public class ResetLoginPasswordActivity
        extends MVPBasicActivity
        implements ResetLoginPasswordContract.View {

    private static final String EXTRA_OPERATION_TYPE = "OperationType";
    private static final String EXTRA_TELEPHONE = "Telephone";

    private static final int OPERATION_TYPE_REGISTER_BY_TELEPHONE = 1;
    private static final int OPERATION_TYPE_RECOVER_TELEPHONE_LOGIN_PASSWORD = 2;

    public static void startActivityToRegisterByTelephone(Context context, String telephone) {
        Intent intent = new Intent(context, ResetLoginPasswordActivity.class);
        intent.putExtra(EXTRA_OPERATION_TYPE, OPERATION_TYPE_REGISTER_BY_TELEPHONE);
        intent.putExtra(EXTRA_TELEPHONE, telephone);
        context.startActivity(intent);
    }

    public static void startActivityToResetTelephoneLoginPasswordBySMS(Context context,
                                                                       String telephone) {
        Intent intent = new Intent(context, ResetLoginPasswordActivity.class);
        intent.putExtra(EXTRA_OPERATION_TYPE, OPERATION_TYPE_RECOVER_TELEPHONE_LOGIN_PASSWORD);
        intent.putExtra(EXTRA_TELEPHONE, telephone);
        context.startActivity(intent);
    }

    private EditText mEtPassword;
    private TextView mTvRuleLength;
    private TextView mTvRuleRepeatedNumbers;
    private TextView mTvRuleCombination;
    private TextView mTvTitle;
    private TextView mTvHint;
    private EditText mEtConfirmPassword;
    private TextView mTvRuleConsistency;
    private ProgressButton mPBtnConfirm;

    private int mOperationType;
    private String mTelephone;


    @Override
    protected int getLayoutID() {
        return R.layout.activity_reset_login_password;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mEtPassword = findViewById(R.id.mEtPassword);
        mTvRuleLength = findViewById(R.id.mTvRuleLength);
        mTvRuleRepeatedNumbers = findViewById(R.id.mTvRuleRepeatedNumbers);
        mTvRuleCombination = findViewById(R.id.mTvRuleCombination);
        mEtConfirmPassword = findViewById(R.id.mEtConfirmPassword);
        mTvRuleConsistency = findViewById(R.id.mTvRuleConsistency);
        mPBtnConfirm = findViewById(R.id.mPBtnConfirm);
        mTvTitle = findViewById(R.id.mTvTitle);
        mTvHint = findViewById(R.id.mTvHint);

        mOperationType = getIntent().getIntExtra(EXTRA_OPERATION_TYPE, -1);
        mTelephone = getIntent().getStringExtra(EXTRA_TELEPHONE);
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        setSystemUiMode(SYSTEM_UI_MODE_TRANSPARENT_LIGHT_BAR_STATUS_AND_NAVIGATION);
        setToolbarId(R.id.mDefaultToolbar, true);

        setupPasswordRuleVerificationAnimation();
        setupMode();

        mPBtnConfirm.setOnClickListener(mOnClickListener);
    }

    private void setupMode() {
        switch (mOperationType) {
            case OPERATION_TYPE_REGISTER_BY_TELEPHONE:
                mTvTitle.setText("注册，设置登录密码");
                mTvHint.setText("注册前需要验证您的手机号码\n请在下方输入您收到的手机验证码并设置登录密码");
                break;
            case OPERATION_TYPE_RECOVER_TELEPHONE_LOGIN_PASSWORD:
                mTvTitle.setText("设置登录密码");
                mTvHint.setText("设置完成之后您将可以使用新的登录密码进行登录");
                break;
        }
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
                mTvRuleConsistency.setEnabled(
                        !TextUtils.isEmpty(s) && TextUtils.equals(s, mEtConfirmPassword.getText()));
            }
        });

        mEtConfirmPassword.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mTvRuleConsistency.setEnabled(
                        !TextUtils.isEmpty(s) && TextUtils.equals(s, mEtPassword.getText()));
            }
        });
    }

    private void confirm() {
        final String newPassword = mEtPassword.getText().toString();

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

        mPBtnConfirm.startHideAnim(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                switch (mOperationType) {
                    case OPERATION_TYPE_REGISTER_BY_TELEPHONE:
                        getPresenter().registerByTelephone(mTelephone, newPassword);
                        break;
                    case OPERATION_TYPE_RECOVER_TELEPHONE_LOGIN_PASSWORD:
                        getPresenter().resetTelephoneLoginPassword(mTelephone, newPassword);
                        break;
                }
            }
        });
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mPBtnConfirm:
                    confirm();
                    break;
            }
        }
    };

    @Override
    public void showRegisterSuccessful() {
        SuccessfulActivity.startActivityForTelephoneRegister(
                this,
                mTelephone,
                mEtPassword.getText().toString());
        finish();
    }

    @Override
    public void showResetLoginPasswordSuccessful() {
        switch (mOperationType) {
            case OPERATION_TYPE_RECOVER_TELEPHONE_LOGIN_PASSWORD:
                SuccessfulActivity.startActivityForRecoverTelephoneLoginPassword(
                        this,
                        mTelephone, mEtPassword.getText().toString());
                break;
        }

        finish();
    }

    @Override
    public void cancelProgressButtonLoadingIfNeed() {
        mPBtnConfirm.startShowAnim();
    }

}
