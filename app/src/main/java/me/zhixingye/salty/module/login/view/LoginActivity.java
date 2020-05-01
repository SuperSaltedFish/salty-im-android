package me.zhixingye.salty.module.login.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import me.zhixingye.salty.R;
import me.zhixingye.salty.basic.BasicCompatActivity;
import me.zhixingye.salty.module.login.contract.LoginContract;
import me.zhixingye.salty.module.login.presenter.LoginPresenter;
import me.zhixingye.salty.module.main.view.MainActivity;
import me.zhixingye.salty.widget.view.PhoneEditText;
import me.zhixingye.salty.widget.view.ProgressButton;

public class LoginActivity
        extends BasicCompatActivity<LoginPresenter>
        implements LoginContract.View {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    private PhoneEditText mPetPhone;
    private TextInputLayout mTilPassword;
    private EditText mEtPassword;
    private ProgressButton mPBtnLogin;
    private Button mBtnRegister;
    private Button mBtnResetPassword;


    @Override
    protected int getLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mPetPhone = findViewById(R.id.mPetPhone);
        mTilPassword = findViewById(R.id.mTilPassword);
        mEtPassword = findViewById(R.id.mEtPassword);
        mPBtnLogin = findViewById(R.id.mPBtnLogin);
        mBtnRegister = findViewById(R.id.mBtnRegister);
        mBtnResetPassword = findViewById(R.id.mBtnResetPassword);
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        setSystemUiMode(SYSTEM_UI_MODE_TRANSPARENT_LIGHT_BAR_STATUS);
        mBtnRegister.setOnClickListener(mOnClickListener);
        mBtnResetPassword.setOnClickListener(mOnClickListener);
        mPBtnLogin.setOnClickListener(mOnClickListener);
    }

    private void tryLogin() {
        final String telephone = mPetPhone.getPhoneSuffixText();
        final String password = mEtPassword.getText().toString();
        if (TextUtils.isEmpty(telephone)) {
            mPetPhone.setError("请输入一个合法的手机号码");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mTilPassword.setError("请输入密码");
            return;
        }

        mPBtnLogin.startHideAnim(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mPresenter.tryLogin(telephone, password);
            }
        });
    }

    private void gotoRegisterActivity() {
        RegisterActivity.startActivity(this);
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mPBtnLogin:
                    tryLogin();
                    break;
                case R.id.mBtnRegister:
                    LoginActivity.this.gotoRegisterActivity();
                    break;
                case R.id.mBtnResetPassword:
                    break;
            }
        }
    };

    @Override
    public void jumpToVerifyPage() {

    }

    @Override
    public void startHomeActivity() {
        MainActivity.startActivity(this);
    }

    @Override
    public void showError(String error) {
        super.showError(error);
        mPBtnLogin.startShowAnim();
    }
}
