package me.zhixingye.salty.module.login.view;

import me.zhixingye.salty.R;
import me.zhixingye.salty.basic.BasicCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class PhoneVerifyCodeActivity extends BasicCompatActivity {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, PhoneVerifyCodeActivity.class));
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_phone_verify_code;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        setSystemUiMode(SYSTEM_UI_MODE_TRANSPARENT_LIGHT_BAR_STATUS_AND_NAVIGATION);
        setDisplayHomeAsUpEnabled(true);
    }

}
