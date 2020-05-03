package me.zhixingye.salty.module.login.view;

import me.zhixingye.salty.R;
import me.zhixingye.salty.basic.BasicCompatActivity;
import me.zhixingye.salty.module.login.contract.RecoverPasswordContract;
import me.zhixingye.salty.module.login.presenter.RecoverPasswordPresenter;
import me.zhixingye.salty.widget.view.PhoneEditText;
import me.zhixingye.salty.widget.view.ProgressButton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

public class RecoverPasswordActivity
        extends BasicCompatActivity<RecoverPasswordPresenter>
        implements RecoverPasswordContract.View {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, RecoverPasswordActivity.class);
        context.startActivity(intent);
    }

    private PhoneEditText mPetPhone;
    private ProgressButton mPBtnNext;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_recover_password;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mPetPhone = findViewById(R.id.mPetPhone);
        mPBtnNext = findViewById(R.id.mPBtnNext);
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        setSystemUiMode(SYSTEM_UI_MODE_TRANSPARENT_LIGHT_BAR_STATUS_AND_NAVIGATION);
        setDisplayHomeAsUpEnabled(true);

        mPBtnNext.setOnClickListener(mOnClickListener);
    }

    private void next() {
        final String telephone = mPetPhone.getPhoneSuffixText();
        if (TextUtils.isEmpty(telephone)) {
            mPetPhone.setError("请输入一个合法的手机号码");
            return;
        }

        mPBtnNext.startHideAnim(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mPresenter.obtainResetTelephoneLoginPasswordSMS(telephone);
            }
        });
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

    @Override
    public void gotoResetTelephoneLoginPage() {
        ResetLoginPasswordActivity.startActivityToResetTelephoneLoginPasswordBySMS(
                this,
                mPetPhone.getPhoneSuffixText());
        mPBtnNext.startShowAnim();
    }

    @Override
    public void showError(String error) {
        super.showError(error);
        mPBtnNext.startShowAnim();
    }
}
