package me.zhixingye.salty.module.login.view;

import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import me.zhixingye.salty.R;
import me.zhixingye.salty.basic.BasicCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SuccessfulActivity extends BasicCompatActivity {

    private static final String EXTRA_OPERATION_TYPE = "OperationType";
    private static final String EXTRA_TELEPHONE = "Telephone";
    private static final String EXTRA_PASSWORD = "Password";

    private static final int OPERATION_TYPE_TELEPHONE_REGISTER = 1;
    private static final int OPERATION_TYPE_RECOVER_TELEPHONE_LOGIN_PASSWORD = 2;

    public static void startActivityForTelephoneRegister(Context context, String telephone, String password) {
        Intent intent = new Intent(context, SuccessfulActivity.class);
        intent.putExtra(EXTRA_OPERATION_TYPE, OPERATION_TYPE_TELEPHONE_REGISTER);
        intent.putExtra(EXTRA_TELEPHONE, telephone);
        intent.putExtra(EXTRA_PASSWORD, password);
        context.startActivity(intent);
    }

    public static void startActivityForRecoverTelephoneLoginPassword(Context context, String telephone, String password) {
        Intent intent = new Intent(context, SuccessfulActivity.class);
        intent.putExtra(EXTRA_OPERATION_TYPE, OPERATION_TYPE_RECOVER_TELEPHONE_LOGIN_PASSWORD);
        intent.putExtra(EXTRA_TELEPHONE, telephone);
        intent.putExtra(EXTRA_PASSWORD, password);
        context.startActivity(intent);
    }

    private ImageView mIvSuccessfulLogo;
    private TextView mTvSuccessfulTitle;
    private TextView mTvSuccessfulHint;
    private Button mBtnPositive;
    private Button mBtnNegative;

    private int mOperationType;
    private String mTelephone;
    private String mPassword;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_register_successful;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mIvSuccessfulLogo = findViewById(R.id.mIvSuccessfulLogo);
        mTvSuccessfulTitle = findViewById(R.id.mTvSuccessfulTitle);
        mTvSuccessfulHint = findViewById(R.id.mTvSuccessfulHint);
        mBtnPositive = findViewById(R.id.mBtnPositive);
        mBtnNegative = findViewById(R.id.mBtnNegative);

        mOperationType = getIntent().getIntExtra(EXTRA_OPERATION_TYPE, -1);
        mTelephone = getIntent().getStringExtra(EXTRA_TELEPHONE);
        mPassword = getIntent().getStringExtra(EXTRA_PASSWORD);
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        setSystemUiMode(SYSTEM_UI_MODE_TRANSPARENT_LIGHT_BAR_STATUS_AND_NAVIGATION);

        mBtnPositive.setOnClickListener(mOnClickListener);
        mBtnNegative.setOnClickListener(mOnClickListener);

        setupHint();
    }

    private void setupHint() {
        switch (mOperationType) {
            case OPERATION_TYPE_TELEPHONE_REGISTER:
                mTvSuccessfulTitle.setText("注册成功");
                mTvSuccessfulHint.setText("恭喜你！您可以立即开始使用咸聊啦");
                mBtnPositive.setText("立即登录");
                mBtnNegative.setText("返回登录页");
                break;
            case OPERATION_TYPE_RECOVER_TELEPHONE_LOGIN_PASSWORD:
                mTvSuccessfulTitle.setText("重置登陆密码成功");
                mTvSuccessfulHint.setText("恭喜你！您可以使用新的密码登陆啦");
                mBtnPositive.setText("立即登录");
                mBtnNegative.setText("返回登录页");
                break;
            default:
                finish();
                return;
        }
        //显示成功动画，放到addIdleHandler中比较好，不然动画一下播完，放到这里，可以稍微慢一拍再播
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                AnimatedVectorDrawableCompat drawableAnim = AnimatedVectorDrawableCompat.create(SuccessfulActivity.this, R.drawable.ic_anim_success);
                if (drawableAnim != null) {
                    mIvSuccessfulLogo.setImageDrawable(drawableAnim);
                    drawableAnim.start();
                }
                return false;
            }
        });
    }

    private void clickPositiveBtn() {
        switch (mOperationType) {
            case OPERATION_TYPE_TELEPHONE_REGISTER:
            case OPERATION_TYPE_RECOVER_TELEPHONE_LOGIN_PASSWORD:
                gotoLoginActivity(
                        mTelephone,
                        mPassword);
                break;
            default:
                finish();
        }
    }

    private void clickNegativeBtn() {
        switch (mOperationType) {
            case OPERATION_TYPE_TELEPHONE_REGISTER:
            case OPERATION_TYPE_RECOVER_TELEPHONE_LOGIN_PASSWORD:
                gotoLoginActivity(null, null);
                break;
            default:
                finish();
        }
    }

    private void gotoLoginActivity(String telephone, String password) {
        LoginActivity.startActivityByTelephoneAccount(this, telephone, password);
        finish();
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mBtnPositive:
                    clickPositiveBtn();
                    break;
                case R.id.mBtnNegative:
                    clickNegativeBtn();
                    break;
            }
        }
    };

}
