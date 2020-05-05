package me.zhixingye.salty.module.splash.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import me.zhixingye.salty.R;
import me.zhixingye.salty.basic.BasicCompatActivity;
import me.zhixingye.salty.module.login.view.LoginActivity;
import me.zhixingye.salty.module.main.view.MainActivity;
import me.zhixingye.salty.module.splash.contract.SplashContract;
import me.zhixingye.salty.module.splash.presenter.SplashPresenter;
import me.zhixingye.salty.util.PermissionHelper;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class SplashActivity extends BasicCompatActivity<SplashPresenter> implements SplashContract.View {

    @Override
    protected int getLayoutID() {
        return R.layout.activity_splash;
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
    public void onBackPressed() {
    }

    @Override
    public void startLoginActivity() {
        LoginActivity.startActivity(this);
        finish();
    }

    @Override
    public void startHomeActivity() {
        MainActivity.startActivity(this);
        finish();
    }

    @Override
    public void startGuideActivity() {
        GuideActivity.startActivity(this);
        finish();
    }

    @Override
    public void showLoginError(String error) {
        showDialog(error, new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                SplashActivity.this.finish();
            }
        });
    }
}
