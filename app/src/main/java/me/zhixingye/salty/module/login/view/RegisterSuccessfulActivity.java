package me.zhixingye.salty.module.login.view;

import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import me.zhixingye.salty.R;
import me.zhixingye.salty.basic.BasicCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class RegisterSuccessfulActivity extends BasicCompatActivity {

    private static final String EXTRA_TELEPHONE = "Telephone";
    private static final String EXTRA_PASSWORD = "Password";

    public static void startActivityByTelephone(Context context, String telephone, String password) {
        Intent intent = new Intent(context, RegisterSuccessfulActivity.class);
        intent.putExtra(EXTRA_TELEPHONE, telephone);
        intent.putExtra(EXTRA_PASSWORD, password);
        context.startActivity(intent);
    }

    private ImageView mIvSuccessfulLogo;
    private Button mBtnLogin;
    private Button mBtnGotoLoginPage;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_register_successful;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mIvSuccessfulLogo = findViewById(R.id.mIvSuccessfulLogo);
        mBtnLogin = findViewById(R.id.mBtnLogin);
        mBtnGotoLoginPage = findViewById(R.id.mBtnGotoLoginPage);
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        setSystemUiMode(SYSTEM_UI_MODE_TRANSPARENT_LIGHT_BAR_STATUS_AND_NAVIGATION);

        mBtnLogin.setOnClickListener(mOnClickListener);
        mBtnGotoLoginPage.setOnClickListener(mOnClickListener);

        //显示成功动画，放到addIdleHandler中比较好，不然动画一下播完，放到这里，可以稍微慢一拍再播
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                AnimatedVectorDrawableCompat drawableAnim = AnimatedVectorDrawableCompat.create(RegisterSuccessfulActivity.this, R.drawable.ic_anim_success);
                if (drawableAnim != null) {
                    mIvSuccessfulLogo.setImageDrawable(drawableAnim);
                    drawableAnim.start();
                }
                return false;
            }
        });
    }

    private void gotoLoginActivity(String telephone, String password) {
        if (!TextUtils.isEmpty(telephone) && !TextUtils.isEmpty(password)) {
            LoginActivity.startActivityByTelephoneAccount(this, telephone, password);
            finish();
        }
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mBtnLogin:
                    gotoLoginActivity(
                            getIntent().getStringExtra(EXTRA_TELEPHONE),
                            getIntent().getStringExtra(EXTRA_PASSWORD));
                    break;
                case R.id.mBtnGotoLoginPage:
                    gotoLoginActivity(null, null);
                    break;
            }
        }
    };

}
