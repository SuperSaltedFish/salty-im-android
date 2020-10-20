package me.zhixingye.salty.module.login.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.salty.protos.SMSOperationType;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.zhixingye.base.component.BasicActivity;
import me.zhixingye.base.view.ProgressButton;
import me.zhixingye.salty.R;
import me.zhixingye.salty.module.login.contract.RecoverPasswordContract;
import me.zhixingye.salty.module.login.presenter.RecoverPasswordPresenter;
import me.zhixingye.salty.widget.view.TelephoneEditText;

public class RecoverPasswordActivity
        extends BasicActivity
        implements RecoverPasswordContract.View {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, RecoverPasswordActivity.class);
        context.startActivity(intent);
    }

    private TelephoneEditText mTEtPhone;
    private ProgressButton mPBtnNext;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_recover_password;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mTEtPhone = findViewById(R.id.mTEtPhone);
        mPBtnNext = findViewById(R.id.mPBtnNext);
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        setSystemUiMode(SYSTEM_UI_MODE_TRANSPARENT_LIGHT_BAR_STATUS_AND_NAVIGATION);
        setToolbarId(R.id.mDefaultToolbar,true);

        mPBtnNext.setOnClickListener(mOnClickListener);
    }

    private void next() {
        final String telephone = mTEtPhone.getPhoneSuffixText();
        if (TextUtils.isEmpty(telephone)) {
            mTEtPhone.setError("请输入一个合法的手机号码");
            return;
        }

        TelephoneSMSVerifyActivity.startActivityForResult(this, 1, telephone, SMSOperationType.RESET_PASSWORD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == TelephoneSMSVerifyActivity.RESULT_CODE_VERIFY_SUCCESSFUL) {
            gotoResetLoginPassword();
        }
    }

    private void gotoResetLoginPassword() {
        ResetLoginPasswordActivity.startActivityToResetTelephoneLoginPasswordBySMS(
                this,
                mTEtPhone.getPhoneSuffixText());
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mPBtnNext:
                    next();
                    break;
            }
        }
    };

    @NonNull
    @Override
    public RecoverPasswordContract.Presenter createPresenterImpl() {
        return new RecoverPasswordPresenter();
    }

    @Override
    public void onPresenterBound() {

    }

    @Override
    public void cancelProgressButtonLoadingIfNeed() {
        mPBtnNext.startShowAnim();
    }
}
