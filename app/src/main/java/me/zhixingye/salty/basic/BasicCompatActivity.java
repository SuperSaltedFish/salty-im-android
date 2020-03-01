package me.zhixingye.salty.basic;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import androidx.annotation.FloatRange;
import androidx.annotation.IntDef;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import me.zhixingye.salty.R;
import me.zhixingye.salty.widget.dialog.AlertDialog;
import me.zhixingye.salty.widget.dialog.ProgressDialog;
import me.zhixingye.salty.widget.listener.Cancelable;

/**
 * Created by YZX on 2017年06月12日.
 * 生命太短暂,不要去做一些根本没有人想要的东西
 */


public abstract class BasicCompatActivity<P extends BasicPresenter> extends AppCompatActivity {

    public static final int SYSTEM_UI_MODE_NONE = 0;//默认显示，无沉侵
    public static final int SYSTEM_UI_MODE_TRANSPARENT_BAR_STATUS = 1;//沉侵状态栏，内容延申到状态栏
    public static final int SYSTEM_UI_MODE_TRANSPARENT_LIGHT_BAR_STATUS = 2;//沉侵状态栏，内容延申到状态栏，并且状态栏内容(如时间)是黑色的
    public static final int SYSTEM_UI_MODE_TRANSPARENT_BAR_STATUS_AND_NAVIGATION = 3;//沉侵状态栏，内容延申到状态栏和导航栏
    public static final int SYSTEM_UI_MODE_TRANSPARENT_LIGHT_BAR_STATUS_AND_NAVIGATION = 4;//沉侵状态栏，内容延申到状态栏和导航栏，并且状态栏和导航栏内容是黑色的
    public static final int SYSTEM_UI_MODE_FULLSCREEN = 5;//全屏
    public static final int SYSTEM_UI_MODE_LIGHT_BAR = 6;//状态栏内容(如时间)是黑色的

    @IntDef({SYSTEM_UI_MODE_NONE
            , SYSTEM_UI_MODE_TRANSPARENT_BAR_STATUS
            , SYSTEM_UI_MODE_TRANSPARENT_LIGHT_BAR_STATUS
            , SYSTEM_UI_MODE_TRANSPARENT_BAR_STATUS_AND_NAVIGATION
            , SYSTEM_UI_MODE_TRANSPARENT_LIGHT_BAR_STATUS_AND_NAVIGATION
            , SYSTEM_UI_MODE_FULLSCREEN
            , SYSTEM_UI_MODE_LIGHT_BAR})
    @Retention(RetentionPolicy.SOURCE)
    private @interface SystemUiMode {
    }

    protected P mPresenter;

    protected InputMethodManager mInputManager;
    protected ProgressDialog mProgressDialog;

    protected Toolbar mDefaultToolbar;

    private Vibrator mVibrator;

    @LayoutRes
    protected abstract int getLayoutID();

    protected abstract void init(Bundle savedInstanceState);

    protected abstract void setup(Bundle savedInstanceState);

    //这里加了final，就是希望子类不要重写这个方法,也没必要重写
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemUiMode(SYSTEM_UI_MODE_TRANSPARENT_BAR_STATUS);

        mVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);

//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);//就算写了这行代码，app一进来的时候默认还是会横屏，onCreate还是会走两次的，这种情况在打开手机自动旋转，在手机横向的情况下打开APP。要想强制某个方向，只能在manifests中写，没其他方法
        int layoutID = getLayoutID();
        if (layoutID != 0) {
            setContentView(layoutID);
            //这里做了全局toolbar的默认初始化，如果有toolbar就会初始化
            mDefaultToolbar = findViewById(R.id.Default_mToolbar);
            if (mDefaultToolbar != null) {
                setSupportActionBar(mDefaultToolbar);
                setTitle("");
            }
        }
        initPresenter();//MVP模式，这里开始初始化presenter,注意了，这个方法应该在init()方法之前。
        init(savedInstanceState);
        setup(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        if (mPresenter != null) {//释放mPresenter
            mPresenter.detachView();
            mPresenter = null;
        }
        if (mProgressDialog != null) {//关闭加载对话框，如果存在
            mProgressDialog.setOnCancelListener(null);
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {//返回键的默认判断
            onBackPressed();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    //设置是否显示返回箭头，这个箭头是系统toolbar默认的那个返回箭头样式
    protected void setDisplayHomeAsUpEnabled(boolean enable) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(enable);
        }
    }

    //初始化Presenter，这里用到了反射，这段不是很好理解，可以断点Debug一行行看代码执行情况以及对应的一些class变量
    @SuppressWarnings("unchecked")
    private void initPresenter() {
        if (this instanceof BasicView) {
            BasicView view = (BasicView) this;
            mPresenter = (P) view.getPresenter();//获取一个Presenter，子类实现了getPresenter();
            if (mPresenter == null) {
                return;
            }
            Class aClass = this.getClass();//获取自己的class，这里的this是子类activity而不是baseActivity
            while (aClass != null) {
                //获取父类，这里的type就是BaseActivity
                Type type = aClass.getGenericSuperclass();
                if (type instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    //获取泛型类型，这里的genericType就是泛型T
                    Type genericType = parameterizedType.getActualTypeArguments()[0];
                    //这里获取Presenter实现的那些接口
                    Class<?>[] interfaces = mPresenter.getClass().getInterfaces();
                    //判断Presenter实现的接口是否有泛型T，本质上就是判断mPresenter是否就是泛型T的实例
                    for (Class c : interfaces) {
                        if (c == genericType) {
                            mPresenter.attachView(view);
                            return;
                        }
                    }
                } else {
                    aClass = aClass.getSuperclass();
                }
            }
            mPresenter = null;
        }
    }

    //设置系统状态栏的样式，比如全屏，或者沉侵式状态栏等等，具体类型可以见每个status的定义，有相关注释
    public void setSystemUiMode(@SystemUiMode int mode) {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        switch (mode) {
            case SYSTEM_UI_MODE_NONE:
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    TypedValue value = new TypedValue();
                    getTheme().resolveAttribute(R.attr.colorPrimary, value, true);
                    window.setStatusBarColor(value.data);
                }
                break;
            case SYSTEM_UI_MODE_TRANSPARENT_BAR_STATUS:
            case SYSTEM_UI_MODE_TRANSPARENT_LIGHT_BAR_STATUS:
                if (mode == SYSTEM_UI_MODE_TRANSPARENT_LIGHT_BAR_STATUS && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Color.TRANSPARENT);
                }
                break;
            case SYSTEM_UI_MODE_TRANSPARENT_BAR_STATUS_AND_NAVIGATION:
            case SYSTEM_UI_MODE_TRANSPARENT_LIGHT_BAR_STATUS_AND_NAVIGATION:
                if (mode == SYSTEM_UI_MODE_TRANSPARENT_LIGHT_BAR_STATUS_AND_NAVIGATION && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                    }
                } else {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setNavigationBarColor(Color.TRANSPARENT);
                    window.setStatusBarColor(Color.TRANSPARENT);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    window.setNavigationBarContrastEnforced(false);
                }
                break;
            case SYSTEM_UI_MODE_FULLSCREEN:
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                break;
            case SYSTEM_UI_MODE_LIGHT_BAR:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
                break;
        }
    }

    //设置亮度
    public void setMinBrightness(@FloatRange(from = 0, to = 1) float paramFloat) {
        int systemBrightness = 0;
        try {
            systemBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException ignored) {
        }

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        if (paramFloat * 255 < systemBrightness || paramFloat < params.screenBrightness) {
            return;
        }
        params.screenBrightness = paramFloat;
        window.setAttributes(params);
    }

    //调用手机震动
    public void vibrate(long milliseconds) {
        mVibrator.vibrate(milliseconds);
    }

    public void showToast(String content) {
        showToast(content, Toast.LENGTH_SHORT);
    }

    public void showLongToast(String content) {
        showToast(content, Toast.LENGTH_LONG);
    }

    public void showToast(String content, int duration) {
        Toast toast = Toast.makeText(this, content, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    //强制开启键盘
    public void showSoftKeyboard(final View focusView) {//一定要等界面可见才可以弹出键盘，否则无效
        if (mInputManager == null) {
            mInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        //但如果View已经测量了，就直接显示键盘
        if (focusView.getWidth() != 0 && focusView.getHeight() != 0) {
            if (!focusView.isFocusable()) {
                return;
            }
            focusView.requestFocus();
            mInputManager.showSoftInput(focusView, 0);
        } else {
            //但如果View没有测量，要等View测量之后才弹出键盘，不然有时候键盘会弹不出
            focusView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (!focusView.isFocusable()) {
                        focusView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        return;
                    }
                    focusView.requestFocus();
                    if (mInputManager.showSoftInput(focusView, 0)) {
                        focusView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            });
        }
    }

    //隐藏软键盘
    public void hideSoftKeyboard() {
        if (mInputManager == null) {
            mInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            View focusView = getCurrentFocus();
            if (focusView != null) {
                focusView.clearFocus();
                mInputManager.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            }
        }
    }

    //是否显示loading对话框，这个方法其实是Contract.View的方法，这里先实现一个默认的方法，每个子类就不需要单独实现它，详情见BaseView
    public void setEnableLoading(boolean isEnable) {
        setEnableLoading(isEnable, null);
    }

    public void setEnableLoading(boolean isEnable, final Cancelable cancelable) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this, getString(R.string.Hint_Loading));
        }
        if (isEnable) {
            //显示对话框之前把键盘隐藏
            hideSoftKeyboard();
            if (cancelable != null) {
                mProgressDialog.setCancelable(true);
                mProgressDialog.setOnCancelListener(dialog -> cancelable.cancel());
            } else {
                mProgressDialog.setCancelable(false);
                mProgressDialog.setOnCancelListener(null);
            }
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        } else {
            mProgressDialog.setOnCancelListener(null);
            mProgressDialog.dismiss();
        }
    }

    //显示一个content内容的对话框，这个方法其实是Contract.View的方法，这里先实现一个默认的方法，每个子类就不需要单独实现它，详情见BaseView
    public void showDialog(String content) {
        showDialog(content, null);
    }

    public void showDialog(String content, DialogInterface.OnDismissListener listener) {
        new AlertDialog(this)
                .setMessage(content)
                .setOnDismissListener(listener)
                .show();
    }

    //显示一个Error错误，默认使用toast实现的，这个方法其实是Contract.View的方法，这里先实现一个默认的方法，每个子类就不需要单独实现它，详情见BaseView
    public void showError(String error) {
        if (TextUtils.isEmpty(error)) {
            return;
        }
        showToast(error);
        vibrate(50);
    }

    //判断mPresenter是否和Contract.View绑定了，这个方法其实是Contract.View的方法，这里先实现一个默认的方法，每个子类就不需要单独实现它，详情见BaseView
    public boolean isAttachedToPresenter() {
        return mPresenter != null;
    }
}
