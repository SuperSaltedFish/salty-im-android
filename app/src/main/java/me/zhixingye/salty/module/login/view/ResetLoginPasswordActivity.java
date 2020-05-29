package me.zhixingye.salty.module.login.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Locale;
import me.zhixingye.salty.R;
import me.zhixingye.salty.basic.BasicCompatActivity;
import me.zhixingye.salty.configure.AppConfig;
import me.zhixingye.salty.module.login.contract.ResetLoginPasswordContract;
import me.zhixingye.salty.module.login.presenter.ResetLoginPasswordPresenter;
import me.zhixingye.salty.util.AnimationUtil;
import me.zhixingye.salty.util.RegexUtil;
import me.zhixingye.salty.widget.listener.SimpleTextWatcher;
import me.zhixingye.salty.widget.view.ProgressButton;

public class ResetLoginPasswordActivity
        extends BasicCompatActivity<ResetLoginPasswordPresenter>
        implements ResetLoginPasswordContract.View {

    private static final String EXTRA_OPERATION_TYPE = "OperationType";
    private static final String EXTRA_TELEPHONE = "Telephone";

    private static final int OPERATION_TYPE_REGISTER_BY_TELEPHONE = 1;
    private static final int OPERATION_TYPE_RECOVER_TELEPHONE_LOGIN_PASSWORD_BY_OLD_PASSWORD = 2;
    private static final int OPERATION_TYPE_RECOVER_TELEPHONE_LOGIN_PASSWORD_BY_SMS = 3;

    public static void startActivityToRegisterByTelephone(Context context, String telephone) {
        Intent intent = new Intent(context, ResetLoginPasswordActivity.class);
        intent.putExtra(EXTRA_OPERATION_TYPE, OPERATION_TYPE_REGISTER_BY_TELEPHONE);
        intent.putExtra(EXTRA_TELEPHONE, telephone);
        context.startActivity(intent);
    }

    public static void startActivityToResetTelephoneLoginPasswordByOldPassword(Context context,
            String telephone) {
        Intent intent = new Intent(context, ResetLoginPasswordActivity.class);
        intent.putExtra(EXTRA_OPERATION_TYPE,
                OPERATION_TYPE_RECOVER_TELEPHONE_LOGIN_PASSWORD_BY_OLD_PASSWORD);
        intent.putExtra(EXTRA_TELEPHONE, telephone);
        context.startActivity(intent);
    }

    public static void startActivityToResetTelephoneLoginPasswordBySMS(Context context,
            String telephone) {
        Intent intent = new Intent(context, ResetLoginPasswordActivity.class);
        intent.putExtra(EXTRA_OPERATION_TYPE,
                OPERATION_TYPE_RECOVER_TELEPHONE_LOGIN_PASSWORD_BY_SMS);
        intent.putExtra(EXTRA_TELEPHONE, telephone);
        context.startActivity(intent);
    }

    private FrameLayout mFlSMSInputLayout;
    private EditText mEtSMSCode;
    private ProgressButton mPBtnResend;
    private EditText mEtOldPassword;
    private TextInputLayout mTilOldPassword;
    private TextInputLayout mTilSMSCode;
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
        mFlSMSInputLayout = findViewById(R.id.mFlSMSInputLayout);
        mEtSMSCode = findViewById(R.id.mEtSMSCode);
        mPBtnResend = findViewById(R.id.mPBtnResend);
        mEtOldPassword = findViewById(R.id.mEtOldPassword);
        mEtPassword = findViewById(R.id.mEtPassword);
        mTvRuleLength = findViewById(R.id.mTvRuleLength);
        mTvRuleRepeatedNumbers = findViewById(R.id.mTvRuleRepeatedNumbers);
        mTvRuleCombination = findViewById(R.id.mTvRuleCombination);
        mEtConfirmPassword = findViewById(R.id.mEtConfirmPassword);
        mTvRuleConsistency = findViewById(R.id.mTvRuleConsistency);
        mTilOldPassword = findViewById(R.id.mTilOldPassword);
        mPBtnConfirm = findViewById(R.id.mPBtnConfirm);
        mTilSMSCode = findViewById(R.id.mTilSMSCode);
        mTvTitle = findViewById(R.id.mTvTitle);
        mTvHint = findViewById(R.id.mTvHint);

        mOperationType = getIntent().getIntExtra(EXTRA_OPERATION_TYPE, -1);
        mTelephone = getIntent().getStringExtra(EXTRA_TELEPHONE);
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        setSystemUiMode(SYSTEM_UI_MODE_TRANSPARENT_LIGHT_BAR_STATUS_AND_NAVIGATION);
        setDisplayHomeAsUpEnabled(true);

        setupPasswordRuleVerificationAnimation();
        setupMode();

        mPBtnConfirm.setOnClickListener(mOnClickListener);
        mPBtnResend.setOnClickListener(mOnClickListener);

        starResendCountDown();
    }

    private void setupMode() {
        switch (mOperationType) {
            case OPERATION_TYPE_REGISTER_BY_TELEPHONE:
                mFlSMSInputLayout.setVisibility(View.VISIBLE);
                mTilOldPassword.setVisibility(View.GONE);
                mTvTitle.setText("注册，设置登录密码");
                mTvHint.setText("注册前需要验证您的手机号码\n请在下方输入您收到的手机验证码并设置登录密码");
                break;
            case OPERATION_TYPE_RECOVER_TELEPHONE_LOGIN_PASSWORD_BY_SMS:
                mFlSMSInputLayout.setVisibility(View.VISIBLE);
                mTilOldPassword.setVisibility(View.GONE);
                mTvTitle.setText("设置登录密码");
                mTvHint.setText("设置完成之后您将可以使用新的登录密码进行登录");
                break;
            case OPERATION_TYPE_RECOVER_TELEPHONE_LOGIN_PASSWORD_BY_OLD_PASSWORD:
                mFlSMSInputLayout.setVisibility(View.GONE);
                mTilOldPassword.setVisibility(View.VISIBLE);
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
        final String oldPassword = mEtOldPassword.getText().toString();
        final String smsCode = mEtSMSCode.getText().toString();
        final String newPassword = mEtPassword.getText().toString();

        switch (mOperationType) {
            case OPERATION_TYPE_RECOVER_TELEPHONE_LOGIN_PASSWORD_BY_OLD_PASSWORD:
                if (TextUtils.isEmpty(oldPassword)) {
                    mTilOldPassword.setError("密码不能为空，请输入密码");
                    return;
                }
                break;
            case OPERATION_TYPE_REGISTER_BY_TELEPHONE:
            case OPERATION_TYPE_RECOVER_TELEPHONE_LOGIN_PASSWORD_BY_SMS:
                if (TextUtils.isEmpty(smsCode)) {
                    mTilSMSCode.setError("验证码不能为空，请输入手机验证码");
                    return;
                }
                break;
            default:
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

        mPBtnConfirm.startHideAnim(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                switch (mOperationType) {
                    case OPERATION_TYPE_REGISTER_BY_TELEPHONE:
                        mPresenter.registerByTelephone(mTelephone, newPassword);
                        break;
                    case OPERATION_TYPE_RECOVER_TELEPHONE_LOGIN_PASSWORD_BY_OLD_PASSWORD:
                        mPresenter.resetTelephoneLoginPasswordByOldPassword(mTelephone, oldPassword,
                                newPassword);
                        break;
                    case OPERATION_TYPE_RECOVER_TELEPHONE_LOGIN_PASSWORD_BY_SMS:
                        mPresenter
                                .resetTelephoneLoginPasswordBySMS(mTelephone, newPassword);
                        break;
                }
            }
        });
    }

    private void resendSMSCode() {
        mPBtnResend.startHideAnim(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                switch (mOperationType) {
                    case OPERATION_TYPE_RECOVER_TELEPHONE_LOGIN_PASSWORD_BY_SMS:
                        mPresenter.obtainResetTelephoneLoginPasswordSMS(mTelephone);
                        break;
                    case OPERATION_TYPE_REGISTER_BY_TELEPHONE:
                        mPresenter.obtainTelephoneRegisterSMS(mTelephone);
                        break;
                }
            }
        });
    }

    private void setAllowResendSMSCode(boolean isAllow) {
        mPBtnResend.setEnabled(isAllow);
        mPBtnResend.setText("重发");
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mPBtnConfirm:
                    confirm();
                    break;
                case R.id.mPBtnResend:
                    resendSMSCode();
                    break;
            }
        }
    };

    private final CountDownTimer mResendCountDown = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            int time = (int) (millisUntilFinished / 1000);
            mPBtnResend.setText(String.format(Locale.getDefault(), "%d秒", time));
        }

        @Override
        public void onFinish() {
            setAllowResendSMSCode(true);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mResendCountDown.cancel();
    }

    @Override
    public void showRegisterSuccessfulPage() {
        SuccessfulActivity.startActivityForTelephoneRegister(
                this,
                mTelephone,
                mEtPassword.getText().toString());
        finish();
    }

    @Override
    public void showResetSuccessfulPage() {
        switch (mOperationType) {
            case OPERATION_TYPE_RECOVER_TELEPHONE_LOGIN_PASSWORD_BY_SMS:
            case OPERATION_TYPE_RECOVER_TELEPHONE_LOGIN_PASSWORD_BY_OLD_PASSWORD:
                SuccessfulActivity.startActivityForRecoverTelephoneLoginPassword(
                        this,
                        mTelephone, mEtPassword.getText().toString());
                break;
        }

        finish();
    }

    @Override
    public void starResendCountDown() {
        mResendCountDown.cancel();
        mResendCountDown.start();
        setAllowResendSMSCode(false);
    }

    @Override
    public void cancelProgressButtonLoadingIfNeed() {
        super.cancelProgressButtonLoadingIfNeed();
        mPBtnResend.startShowAnim();
        mPBtnConfirm.startShowAnim();
    }

}
