package me.zhixingye.salty.module.splash.view;

import android.content.Intent;
import android.os.Bundle;

import me.zhixingye.salty.basic.BasicCompatActivity;
import me.zhixingye.salty.module.login.view.LoginActivity;
import me.zhixingye.salty.module.splash.contract.SplashContract;
import me.zhixingye.salty.module.splash.presenter.SplashPresenter;
import me.zhixingye.salty.util.PermissionHelper;

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

        PermissionHelper.requestExternalStoragePermissions(
                getSupportFragmentManager(),
                new PermissionHelper.OnPermissionsResult() {
                    @Override
                    public void onGranted() {
                        mPresenter.checkLoginState(getApplicationContext());
                    }

                    @Override
                    public void onDenied(String[] deniedPermissions) {
                        finish();
                    }
                },
                true);
    }

    @Override
    public void startLoginActivity() {
        LoginActivity.startActivity(this);
        finish();
    }

    @Override
    public void startHomeActivity() {
        finish();
    }

    @Override
    public void startGuideActivity() {
        GuideActivity.startActivity(this);
        finish();
    }

    @Override
    public void showLoginError(String error) {

    }

    @Override
    public SplashContract.Presenter getPresenter() {
        return new SplashPresenter();
    }
}
