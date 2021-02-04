package me.zhixingye.salty.module.login.view;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import me.zhixingye.base.component.mvvm.BasicViewModel;
import me.zhixingye.base.component.mvvm.MVVMActivity;
import me.zhixingye.base.listener.OnDialogOnlySingleClickListener;
import me.zhixingye.base.view.SMSCodeEditView;
import me.zhixingye.salty.R;
import me.zhixingye.salty.configure.AppConfig;
import me.zhixingye.salty.module.login.viewmodel.TelephoneSMSVerifyViewModel;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.salty.protos.SMSOperationType;
import com.salty.protos.StatusCode;

import java.util.Locale;

public class TelephoneSMSVerifyActivity extends MVVMActivity {

    public static final int RESULT_CODE_VERIFY_SUCCESSFUL = TelephoneSMSVerifyActivity.class.hashCode();

    private static final String EXTRA_SMS_OPERATION_TYPE = "SMSOperationType";
    private static final String EXTRA_TELEPHONE = "Telephone";

    public static void startActivityForResult(
            Activity context,
            int requestCode,
            String telephone,
            SMSOperationType type) {
        Intent intent = new Intent(context, TelephoneSMSVerifyActivity.class);
        intent.putExtra(EXTRA_TELEPHONE, telephone);
        intent.putExtra(EXTRA_SMS_OPERATION_TYPE, type);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_telephone_sms_verify;
    }

    private TextView mTvSMSCodeSendHint;
    private TextView mTvVoiceSMSHint;
    private SMSCodeEditView mSMSEditView;
    private Button mBtnResend;

    private SMSOperationType mSMSOperationType;
    private String mTelephone;

    private TelephoneSMSVerifyViewModel mTelephoneSMSVerifyViewModel;

    @Override
    protected void init(Bundle savedInstanceState) {
        mTvSMSCodeSendHint = findViewById(R.id.mTvSMSCodeSendHint);
        mTvVoiceSMSHint = findViewById(R.id.mTvVoiceSMSHint);
        mSMSEditView = findViewById(R.id.mSMSEditView);
        mBtnResend = findViewById(R.id.mBtnResend);

        mSMSOperationType = (SMSOperationType) getIntent().getSerializableExtra(EXTRA_SMS_OPERATION_TYPE);
        mTelephone = getIntent().getStringExtra(EXTRA_TELEPHONE);
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        if (mSMSOperationType == SMSOperationType.UNRECOGNIZED
                || mSMSOperationType == SMSOperationType.UNDEFINED
                || TextUtils.isEmpty(mTelephone)) {
            finish();
            return;
        }
        setSystemUiMode(SYSTEM_UI_MODE_TRANSPARENT_LIGHT_BAR_STATUS_AND_NAVIGATION);
        setToolbarId(R.id.mDefaultToolbar, true);

        mBtnResend.setOnClickListener(mOnClickListener);

        setupViewModule();
        setupTitleHint();
        setupVoiceSMSHint();
        setupAutoConfirm();
        resendSMS();
    }

    private void setupViewModule() {
        mTelephoneSMSVerifyViewModel = createViewModel(TelephoneSMSVerifyViewModel.class);

        mTelephoneSMSVerifyViewModel.getSMSLengthData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer data) {
                if (data == null) {
                    return;
                }
                showSMSInputLayout(data);
            }
        });
        mTelephoneSMSVerifyViewModel.getObtainSMSErrorData().observe(this, new Observer<BasicViewModel.ErrorData>() {
            @Override
            public void onChanged(BasicViewModel.ErrorData data) {
                if (data == null) {
                    return;
                }
                mSMSEditView.cleanInputContent();
                StatusCode statusCode = StatusCode.forNumber(data.mErrorCode);
                if (statusCode == null) {
                    showErrorDialog(data.mErrorContent);
                    return;
                }
                switch (statusCode) {
                    case STATUS_ACCOUNT_EXISTS:
                        showRegisteredHintDialog();
                        break;
                    case STATUS_ACCOUNT_NOT_EXISTS:
                        showUnregisteredHintDialog();
                        break;
                    default:
                        showErrorDialog(data.mErrorContent);
                        break;
                }
            }
        });
        mTelephoneSMSVerifyViewModel.getVerifySMSSuccessData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean data) {
                if (Boolean.TRUE.equals(data)) {
                    showVerifySuccessful();
                } else {
                    mSMSEditView.cleanInputContent();
                }
            }
        });
        mTelephoneSMSVerifyViewModel.getVerifySMSSErrorData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String data) {
                if (!TextUtils.isEmpty(data)) {
                    showError(data);
                }
                mSMSEditView.cleanInputContent();
            }
        });
    }

    private void setupTitleHint() {
        String hintPrefix = "一条手机验证码正在发送至 ";
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
            return;
        }
        mTelephoneSMSVerifyViewModel.verifyTelephoneSMS(mTelephone, smsCode, mSMSOperationType);
    }

    private void resendSMS() {
        mTelephoneSMSVerifyViewModel.obtainTelephoneSMS(mTelephone, mSMSOperationType);
    }

    private void setAllowResendSMSCode(boolean isAllow) {
        mBtnResend.setEnabled(isAllow);
        mBtnResend.setText("重新发送验证码");
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.mBtnResend) {
                resendSMS();
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

    private void showSMSInputLayout(int smsCodeLength) {
        setAllowResendSMSCode(false);
        mResendCountDown.cancel();
        mResendCountDown.start();
        mSMSEditView.setVisibility(View.VISIBLE);
        mSMSEditView.setItemCount(smsCodeLength);
        mSMSEditView.cleanInputContent();
        showSoftKeyboard(mSMSEditView.getChildAt(0));
    }

    private void showErrorDialog(String error) {
        showHintDialog(error, new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
    }

    private void showVerifySuccessful() {
        setResult(RESULT_CODE_VERIFY_SUCCESSFUL);
        finish();
    }

    private void showRegisteredHintDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("无法注册")
                .setMessage("该手机账号已经被注册，是否马上去登录?")
                .setNegativeButton("取消", null)
                .setPositiveButton("去登录", new OnDialogOnlySingleClickListener() {
                    @Override
                    public void onSingleClick(DialogInterface dialog, int which) {
                        LoginActivity.startActivityByTelephoneAccount(
                                TelephoneSMSVerifyActivity.this,
                                mTelephone,
                                null);
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                })
                .show();
    }

    private void showUnregisteredHintDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("无法重置密码")
                .setMessage("该手机账号未注册，是否马上去注册?")
                .setNegativeButton("取消", null)
                .setPositiveButton("去注册", new OnDialogOnlySingleClickListener() {
                    @Override
                    public void onSingleClick(DialogInterface dialog, int which) {
                        RegisterActivity.startActivity(TelephoneSMSVerifyActivity.this,
                                mTelephone);
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                })
                .show();
    }
}
