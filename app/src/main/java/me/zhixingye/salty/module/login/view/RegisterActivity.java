package me.zhixingye.salty.module.login.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import androidx.core.content.ContextCompat;
import me.zhixingye.salty.R;
import me.zhixingye.salty.basic.BasicCompatActivity;
import me.zhixingye.salty.module.login.contract.RegisterContract;
import me.zhixingye.salty.module.login.presenter.RegisterPresenter;
import me.zhixingye.salty.widget.view.TelephoneEditText;
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

    private TelephoneEditText mTEtPhone;
    private ProgressButton mPBtnRegister;
    private TextView mTvAgreement;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_register;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mTEtPhone = findViewById(R.id.mTEtPhone);
        mPBtnRegister = findViewById(R.id.mPBtnRegister);
        mTvAgreement = findViewById(R.id.mTvAgreement);
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        setSystemUiMode(SYSTEM_UI_MODE_TRANSPARENT_LIGHT_BAR_STATUS_AND_NAVIGATION);
        setDisplayHomeAsUpEnabled(true);

        setupUserAgreement();

        mPBtnRegister.setOnClickListener(mOnClickListener);

    }

    private void setupUserAgreement() {
        String userAgreement = "注册即代表您同意";
        String focus = "《咸鱼协议》";
        int startIndex = userAgreement.length();
        SpannableString sStr = new SpannableString(userAgreement + focus);
        sStr.setSpan(
                new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorAccent)),
                startIndex,
                startIndex + focus.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mTvAgreement.setText(sStr);
    }

    private void tryNext() {
        final String telephone = mTEtPhone.getPhoneSuffixText();
        if (TextUtils.isEmpty(telephone)) {
            mTEtPhone.setError("请输入一个合法的手机号码");
            return;
        }

        mPBtnRegister.startHideAnim(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mPresenter.obtainTelephoneRegisterSMS(telephone);
            }
        });
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mPBtnRegister:
                    tryNext();
                    break;
            }
        }
    };

    @Override
    public void startPhoneVerifyActivity() {
        mPBtnRegister.startShowAnim();
        ResetLoginPasswordActivity.startActivityToRegisterByTelephone(
                this,
                mTEtPhone.getPhoneSuffixText());
    }

    @Override
    public void showAlreadyRegisterDialog() {
        mPBtnRegister.startShowAnim();
        new MaterialAlertDialogBuilder(this)
                .setTitle("无法注册")
                .setMessage("该手机账号已经被注册，是否马上去登录?")
                .setNegativeButton("取消", null)
                .setPositiveButton("去登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoginActivity.startActivityByTelephoneAccount(
                                RegisterActivity.this,
                                mTEtPhone.getPhoneSuffixText(),
                                null);
                    }
                })
                .show();
    }

    @Override
    public void showError(String error) {
        super.showError(error);
        mPBtnRegister.startShowAnim();
    }
}
