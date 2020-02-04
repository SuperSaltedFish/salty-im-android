package me.zhixingye.salty.module.splash.view;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import me.zhixingye.salty.basic.BasicCompatActivity;
import me.zhixingye.salty.module.login.view.LoginActivity;
import me.zhixingye.salty.module.splash.contract.SplashContract;
import me.zhixingye.salty.module.splash.presenter.SplashPresenter;

public class SplashActivity extends BasicCompatActivity<SplashContract.Presenter> implements SplashContract.View {

    @Override
    protected int getLayoutID() {
        return 0;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        if (!isTaskRoot()) {
            final Intent intent = getIntent();
            final String intentAction = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null && intentAction.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
        setSystemUiMode(SYSTEM_UI_MODE_TRANSPARENT_BAR_STATUS_AND_NAVIGATION);
        new Handler().postDelayed(() -> requestPermissionsInCompatMode(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, 0), 2000);
    }

    @Override
    protected void onRequestPermissionsResult(int requestCode, boolean isSuccess, String[] deniedPermissions) {
        if (isSuccess) {
            mPresenter.checkLoginState(this.getApplicationContext());
        } else {
            finish();
        }
    }

    @Override
    public void startLoginActivity() {
        LoginActivity.startActivity(this);
    }

    @Override
    public void startHomeActivity() {

    }

    @Override
    public void startGuideActivity() {
        GuideActivity.startActivity(this);
    }

    @Override
    public void showLoginError(String error) {

    }

    @Override
    public SplashContract.Presenter getPresenter() {
        return new SplashPresenter();
    }
}
