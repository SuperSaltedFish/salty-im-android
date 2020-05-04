package me.zhixingye.salty.module.login.view;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import me.zhixingye.salty.R;
import me.zhixingye.salty.basic.BasicCompatActivity;
import me.zhixingye.salty.configure.AppConfig;
import me.zhixingye.salty.module.login.contract.TelephoneSMSVerifyContract;
import me.zhixingye.salty.module.login.presenter.TelephoneSMSVerifyPresenter;
import me.zhixingye.salty.module.main.view.MainActivity;
import me.zhixingye.salty.widget.view.SMSCodeEditView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class TelephoneSMSVerifyActivity
        extends BasicCompatActivity<TelephoneSMSVerifyPresenter>
        implements TelephoneSMSVerifyContract.View {

    private static final String EXTRA_OPERATION_TYPE = "OperationType";
    private static final String EXTRA_TELEPHONE = "Telephone";
    private static final String EXTRA_PASSWORD = "Password";

    private static final int OPERATION_TYPE_LOGIN_BY_TELEPHONE = 2;

    public static void startActivityToLogin(Context context, String telephone, String password) {
        Intent intent = new Intent(context, TelephoneSMSVerifyActivity.class);
        intent.putExtra(EXTRA_OPERATION_TYPE, OPERATION_TYPE_LOGIN_BY_TELEPHONE);
        intent.putExtra(EXTRA_TELEPHONE, telephone);
        intent.putExtra(EXTRA_PASSWORD, password);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_telephone_sms_verify;
    }

    private TextView mTvSMSCodeSendHint;
    private TextView mTvVoiceSMSHint;
    private SMSCodeEditView mSMSEditView;
    private Button mBtnResend;

    private int mOperationType;
    private String mTelephone;
    private String mPassword;

    @Override
    protected void init(Bundle savedInstanceState) {
        mTvSMSCodeSendHint = findViewById(R.id.mTvSMSCodeSendHint);
        mTvVoiceSMSHint = findViewById(R.id.mTvVoiceSMSHint);
        mSMSEditView = findViewById(R.id.mSMSEditView);
        mBtnResend = findViewById(R.id.mBtnResend);

        mOperationType = getIntent().getIntExtra(EXTRA_OPERATION_TYPE, -1);
        mTelephone = getIntent().getStringExtra(EXTRA_TELEPHONE);
        mPassword = getIntent().getStringExtra(EXTRA_PASSWORD);
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        setSystemUiMode(SYSTEM_UI_MODE_TRANSPARENT_LIGHT_BAR_STATUS_AND_NAVIGATION);
        setDisplayHomeAsUpEnabled(true);

        mBtnResend.setOnClickListener(mOnClickListener);

        setupTitleHint();
        setupVoiceSMSHint();
        setupAutoConfirm();

        showCountDown();

        showSoftKeyboard(mSMSEditView.getChildAt(0));
    }

    private void setupTitleHint() {
        String hintPrefix = "一条手机验证码已发送至 ";
        String telephone = "+86 " + mTelephone;
        int startIndex = hintPrefix.length();
        SpannableString sStr = new SpannableString(hintPrefix + telephone);
        sStr.setSpan(
                new ForegroundColorSpan(ContextCompat.getColor(this, R.color.textColorPrimary)) {
                    @Override
                    public void updateDrawState(@NonNull TextPaint textPaint) {
                        super.updateDrawState(textPaint);
                        textPaint.setFakeBoldText(true);
                    }
                },
                startIndex,
                startIndex + telephone.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mTvSMSCodeSendHint.setText(sStr);
    }

    private void setupVoiceSMSHint() {
        String hintPrefix = "收不到验证码？";
        String voiceSMS = "试试语音验证码";
        int startIndex = hintPrefix.length();
        SpannableString sStr = new SpannableString(hintPrefix + voiceSMS);
        sStr.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {

            }

        }, startIndex, startIndex + voiceSMS.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mTvVoiceSMSHint.setText(sStr);
    }

    private void setupAutoConfirm() {
        mSMSEditView.setOnInputListener(new SMSCodeEditView.OnInputListener() {
            @Override
            public void onInputComplete(String content) {
                confirm(content);
            }

            @Override
            public void onInputChange(String content) {

            }
        });
    }

    private void confirm(String smsCode) {
        if (TextUtils.isEmpty(smsCode) || smsCode.length() < AppConfig.PHONE_VERIFY_CODE_LENGTH) {
            showError("请输入完整的验证码");
        }

        switch (mOperationType) {
            case OPERATION_TYPE_LOGIN_BY_TELEPHONE:
                mPresenter.loginByTelephone(mTelephone, mPassword, smsCode);
                break;
            default:
                finish();
        }
    }

    private void resendSMS() {
        switch (mOperationType) {
            case OPERATION_TYPE_LOGIN_BY_TELEPHONE:
                mPresenter.obtainLoginTelephoneSMS(mTelephone);
                break;
            default:
                finish();
        }
    }

    private void setAllowResendSMSCode(boolean isAllow) {
        mBtnResend.setEnabled(isAllow);
        mBtnResend.setText("重新发送验证码");
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mBtnResend:
                    resendSMS();
                    break;
            }
        }
    };

    private final CountDownTimer mResendCountDown = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            int time = (int) (millisUntilFinished / 1000);
            mBtnResend.setText(String.format(Locale.getDefault(), "%d秒后可重发验证码", time));
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
    public void showCountDown() {
        setAllowResendSMSCode(false);
        mResendCountDown.cancel();
        mResendCountDown.start();
    }

    @Override
    public void startHomeActivity() {
        MainActivity.startActivity(this);
        finish();
    }

    @Override
    public void showError(String error) {
        super.showError(error);
        mSMSEditView.cleanInputContent();
    }
}
