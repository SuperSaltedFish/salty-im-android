package me.zhixingye.salty.module.login.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import com.google.android.material.textfield.TextInputLayout;
import com.salty.protos.SMSOperationType;
import com.salty.protos.StatusCode;

import me.zhixingye.base.component.mvvm.MVVMActivity;
import me.zhixingye.base.view.ProgressButton;
import me.zhixingye.salty.R;
import me.zhixingye.salty.module.login.viewmodel.LoginViewModel;
import me.zhixingye.salty.module.main.view.MainActivity;
import me.zhixingye.salty.util.AnimationUtil;
import me.zhixingye.salty.widget.view.TelephoneEditText;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class LoginActivity extends MVVMActivity {

    private static final String EXTRA_TELEPHONE = "Telephone";
    private static final String EXTRA_PASSWORD = "Password";

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void startActivityByTelephoneAccount(
            Context context,
            String telephone,
            String password) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(EXTRA_TELEPHONE, telephone);
        intent.putExtra(EXTRA_PASSWORD, password);
        context.startActivity(intent);
    }

    private TelephoneEditText mTEtPhone;
    private TextInputLayout mTilPassword;
    private EditText mEtPassword;
    private ProgressButton mPBtnLogin;
    private Button mBtnRegister;
    private Button mBtnResetPassword;

    private LoginViewModel mLoginViewModel;


    @Override
    protected int getLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mTEtPhone = findViewById(R.id.mTEtPhone);
        mTilPassword = findViewById(R.id.mTilPassword);
        mEtPassword = findViewById(R.id.mEtPassword);
        mPBtnLogin = findViewById(R.id.mPBtnLogin);
        mBtnRegister = findViewById(R.id.mBtnRegister);
        mBtnResetPassword = findViewById(R.id.mBtnResetPassword);
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        setSystemUiMode(SYSTEM_UI_MODE_TRANSPARENT_LIGHT_BAR_STATUS_AND_NAVIGATION);
        mBtnRegister.setOnClickListener(mOnClickListener);
        mBtnResetPassword.setOnClickListener(mOnClickListener);
        mPBtnLogin.setOnClickListener(mOnClickListener);

        setupViewModule();
    }

    private void setupViewModule() {
        mLoginViewModel = createViewModel(LoginViewModel.class);
        mLoginViewModel.getLoginSuccessData().observe(this, new Observer<LoginViewModel.LoginResult>() {
            @Override
            public void onChanged(LoginViewModel.LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                if (loginResult.isLoginSucceed) {
                    startHomeActivity();
                    return;
                }
                if (loginResult.code == StatusCode.STATUS_ACCOUNT_AUTHORIZED_REQUIRED) {
                    startPhoneVerifyActivity();
                    return;
                }
            }
        });
        mLoginViewModel.getLoginLoadingData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean data) {
                if (Boolean.TRUE.equals(data)) {
                    mPBtnLogin.startHideAnim();
                } else {
                    mPBtnLogin.startShowAnim();
                }
            }
        });
    }

    private void tryLogin(Intent intent) {
        String telephone = intent.getStringExtra(EXTRA_TELEPHONE);
        String password = intent.getStringExtra(EXTRA_PASSWORD);
        if (TextUtils.isEmpty(telephone) || TextUtils.isEmpty(password)) {
            return;
        }
        mTEtPhone.setPhoneSuffixText(telephone);
        mEtPassword.setText(password);
        tryLogin();
    }

    private void tryLogin() {
        final String telephone = mTEtPhone.getPhoneSuffixText();
        final String password = mEtPassword.getText().toString();
        if (TextUtils.isEmpty(telephone)) {
            mTEtPhone.setError("请输入一个合法的手机号码");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mTilPassword.setError("密码不能为空，请输入密码");
            return;
        }
        login(telephone, password);
    }

    private void login(final String telephone, final String password) {
        if (TextUtils.isEmpty(telephone)) {
            return;
        }
        mTEtPhone.setPhoneSuffixText(telephone);
        if (TextUtils.isEmpty(password)) {
            showSoftKeyboard(mEtPassword);
            return;
        }
        mTEtPhone.setPhoneSuffixText(telephone);
        mEtPassword.setText(password);
        mPBtnLogin.startHideAnim(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginViewModel.loginByTelephone(telephone, password);
            }
        });
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.mPBtnLogin) {
                tryLogin();
                return;
            }
            if (v.getId() == R.id.mBtnRegister) {
                startRegisterActivity();
                return;
            }
            if (v.getId() == R.id.mBtnResetPassword) {
                startRecoverPasswordActivity();
                return;
            }
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        tryLogin(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == TelephoneSMSVerifyActivity.RESULT_CODE_VERIFY_SUCCESSFUL) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    tryLogin();
                }
            }, 3000);
        }
    }


    private void startPhoneVerifyActivity() {
        TelephoneSMSVerifyActivity.startActivityForResult(
                this,
                1,
                mTEtPhone.getPhoneSuffixText(),
                SMSOperationType.LOGIN);
    }

    private void startRegisterActivity() {
        RegisterActivity.startActivity(this, null);
    }

    private void startRecoverPasswordActivity() {
        RecoverPasswordActivity.startActivity(this);
    }

    private void startHomeActivity() {
        setSystemUiMode(SYSTEM_UI_MODE_TRANSPARENT_BAR_STATUS);
        AnimationUtil.circularRevealShowByFullActivityAnim(
                this,
                mPBtnLogin,
                new ColorDrawable(
                        ContextCompat.getColor(this, R.color.colorAccent)),
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        MainActivity.startActivityByColorTransition(
                                LoginActivity.this,
                                R.color.colorAccent);
                    }
                });
    }

}
