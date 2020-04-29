package me.zhixingye.salty.module.login.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import me.zhixingye.salty.R;
import me.zhixingye.salty.basic.BasicCompatActivity;
import me.zhixingye.salty.widget.view.PhoneEditText;
import me.zhixingye.salty.widget.view.ProgressButton;

public class LoginActivity extends BasicCompatActivity {

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
        mPetPhone.setOnClickListener(mOnClickListener);
        mBtnRegister.setOnClickListener(mOnClickListener);
        mBtnResetPassword.setOnClickListener(mOnClickListener);
    }

    private void gotoRegisterActivity() {
        RegisterActivity.startActivity(this);
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mPetPhone:
                    break;
                case R.id.mBtnRegister:
                    LoginActivity.this.gotoRegisterActivity();
                    break;
                case R.id.mBtnResetPassword:
                    break;
            }
        }
    };
}
