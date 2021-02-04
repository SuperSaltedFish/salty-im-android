package me.zhixingye.salty.module.splash.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.Observer;

import me.zhixingye.base.component.mvvm.MVVMActivity;
import me.zhixingye.salty.R;
import me.zhixingye.salty.module.login.view.LoginActivity;
import me.zhixingye.salty.module.main.view.MainActivity;
import me.zhixingye.salty.module.splash.viewmodel.SplashViewModel;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class SplashActivity extends MVVMActivity {

    private SplashViewModel mSplashViewModel;

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

        setupViewModule();
        checkLoginState();
    }

    private void setupViewModule() {
        mSplashViewModel = createViewModel(SplashViewModel.class);
        mSplashViewModel.getLoginStateData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer data) {
                if (data == null) {
                    return;
                }
                switch (data) {
                    case SplashViewModel.STATE_LOGGED:
                        startHomeActivity();
                        break;
                    case SplashViewModel.STATE_LOGOUT:
                        startLoginActivity();
                        break;
                    case SplashViewModel.STATE_LOGOUT_FIRST:
                        startGuideActivity();
                        break;
                }
            }
        });
    }

    private void checkLoginState() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                mSplashViewModel.checkLoginState();
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
    }

    public void startLoginActivity() {
        LoginActivity.startActivity(this);
        finish();
    }

    public void startHomeActivity() {
        MainActivity.startActivityByImageTransition(this, R.drawable.src_bg_splash);
        finish();
    }

    public void startGuideActivity() {
        GuideActivity.startActivity(this);
        finish();
    }

}
