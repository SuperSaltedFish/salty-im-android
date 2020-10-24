package me.zhixingye.salty.module.login.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.salty.protos.SMSOperationType;

import androidx.annotation.Nullable;
import me.zhixingye.base.component.BasicActivity;
import me.zhixingye.base.component.mvp.MVPBasicActivity;
import me.zhixingye.base.view.ProgressButton;
import me.zhixingye.salty.R;
import me.zhixingye.salty.configure.AppConfig;
import me.zhixingye.salty.module.login.contract.RegisterContract;
import me.zhixingye.salty.module.login.presenter.RegisterPresenter;
import me.zhixingye.salty.module.web.WebActivity;
import me.zhixingye.salty.widget.view.TelephoneEditText;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class RegisterActivity
        extends MVPBasicActivity
        implements RegisterContract.View {

    private static final String TAG = "RegisterActivity";

    private static final String EXTRA_TELEPHONE = "Telephone";

    public static void startActivity(Context context, String telephone) {
        Intent intent = new Intent(context, RegisterActivity.class);
        intent.putExtra(EXTRA_TELEPHONE, telephone);
        context.startActivity(intent);
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
        setToolbarId(R.id.mDefaultToolbar,true);

        setupUserAgreement();

        mPBtnRegister.setOnClickListener(mOnClickListener);

        String telephone = getIntent().getStringExtra(EXTRA_TELEPHONE);
        if (!TextUtils.isEmpty(telephone)) {
            mTEtPhone.setPhoneSuffixText(telephone);
        }

    }

    private void setupUserAgreement() {
        String userAgreement = "注册即代表您同意";
        String focus = "《咸鱼协议》";
        int startIndex = userAgreement.length();
        SpannableString sStr = new SpannableString(userAgreement + focus);
        sStr.setSpan(
                new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        WebActivity
                                .startActivity(RegisterActivity.this, AppConfig.USER_AGREEMENT_URL);
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setUnderlineText(false);
                    }
                },
                startIndex,
                startIndex + focus.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mTvAgreement.setHighlightColor(Color.TRANSPARENT);
        mTvAgreement.setMovementMethod(LinkMovementMethod.getInstance());
        mTvAgreement.setText(sStr);
    }

    private void tryNext() {
        final String telephone = mTEtPhone.getPhoneSuffixText();
        if (TextUtils.isEmpty(telephone)) {
            mTEtPhone.setError("请输入一个合法的手机号码");
            return;
        }

        TelephoneSMSVerifyActivity.startActivityForResult(this, 1, telephone, SMSOperationType.REGISTER);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == TelephoneSMSVerifyActivity.RESULT_CODE_VERIFY_SUCCESSFUL) {
            ResetLoginPasswordActivity.startActivityToRegisterByTelephone(
                    this,
                    mTEtPhone.getPhoneSuffixText());
        }
    }
}
