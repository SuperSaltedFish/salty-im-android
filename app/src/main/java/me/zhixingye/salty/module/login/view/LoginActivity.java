package me.zhixingye.salty.module.login.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.textfield.TextInputLayout;

import me.zhixingye.salty.R;
import me.zhixingye.salty.basic.BasicCompatActivity;

public class LoginActivity extends BasicCompatActivity {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    private TextInputLayout mTilPassword;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mTilPassword = findViewById(R.id.mTilPassword);
    }

    @Override
    protected void setup(Bundle savedInstanceState) {

    }
}
