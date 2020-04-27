package me.zhixingye.salty.module.login.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import me.zhixingye.salty.R;
import me.zhixingye.salty.basic.BasicCompatActivity;

public class RegisterActivity extends BasicCompatActivity {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, RegisterActivity.class));
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_register;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        setSystemUiMode(SYSTEM_UI_MODE_TRANSPARENT_LIGHT_BAR_STATUS);
    }

}
