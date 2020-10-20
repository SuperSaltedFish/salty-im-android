package me.zhixingye.base.component;

import android.content.Context;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import androidx.annotation.IdRes;
import androidx.annotation.IntDef;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import me.zhixingye.base.R;
import me.zhixingye.base.view.TextInputLayoutExtra;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */


public abstract class BasicActivity
        extends AppCompatActivity
        implements UIComponent {

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

    private Toolbar mDefaultToolbar;

    @LayoutRes
    protected abstract int getLayoutID();

    protected abstract void init(Bundle savedInstanceState);

    protected abstract void setup(Bundle savedInstanceState);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int layoutID = getLayoutID();
        if (layoutID != 0) {
            setContentView(layoutID);
        }

        init(savedInstanceState);
        setup(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context,
                             @NonNull AttributeSet attrs) {
        if (TextUtils.equals(TextInputLayout.class.getName(), name)) {
            return new TextInputLayoutExtra(context, attrs);
        }
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //返回键的默认判断
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    protected void setToolbarId(@IdRes int id, boolean displayHomeAsUpEnabled) {
        mDefaultToolbar = findViewById(id);
        setSupportActionBar(mDefaultToolbar);
        setTitle("");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(displayHomeAsUpEnabled);
        }
    }

    protected Toolbar getToolbar() {
        return mDefaultToolbar;
    }

    public Context getContext(){
        return this;
    }

    //设置系统状态栏的样式，比如全屏，或者沉侵式状态栏等等，具体类型可以见每个status的定义，有相关注释
    public void setSystemUiMode(@SystemUiMode int mode) {
        Window window = getWindow();
        window.clearFlags(LayoutParams.FLAG_TRANSLUCENT_STATUS
                | LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        switch (mode) {
            case SYSTEM_UI_MODE_NONE:
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                    TypedValue value = new TypedValue();
                    getTheme().resolveAttribute(R.attr.colorPrimary, value, true);
                    window.setStatusBarColor(value.data);
                }
                break;
            case SYSTEM_UI_MODE_TRANSPARENT_BAR_STATUS:
            case SYSTEM_UI_MODE_TRANSPARENT_LIGHT_BAR_STATUS:
                if (mode == SYSTEM_UI_MODE_TRANSPARENT_LIGHT_BAR_STATUS
                        && VERSION.SDK_INT >= VERSION_CODES.M) {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                }
                if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                    window.addFlags(LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Color.TRANSPARENT);
                }
                break;
            case SYSTEM_UI_MODE_TRANSPARENT_BAR_STATUS_AND_NAVIGATION:
            case SYSTEM_UI_MODE_TRANSPARENT_LIGHT_BAR_STATUS_AND_NAVIGATION:
                if (mode == SYSTEM_UI_MODE_TRANSPARENT_LIGHT_BAR_STATUS_AND_NAVIGATION
                        && VERSION.SDK_INT >= VERSION_CODES.M) {
                    if (VERSION.SDK_INT >= VERSION_CODES.O) {
                        window.getDecorView().setSystemUiVisibility(
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                                        | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                    } else if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                        window.getDecorView().setSystemUiVisibility(
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        window.getDecorView().setSystemUiVisibility(
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                    }
                } else {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                }
                if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                    window.addFlags(LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setNavigationBarColor(Color.TRANSPARENT);
                    window.setStatusBarColor(Color.TRANSPARENT);
                }
                if (VERSION.SDK_INT >= VERSION_CODES.Q) {
                    window.setNavigationBarContrastEnforced(false);
                }
                break;
            case SYSTEM_UI_MODE_FULLSCREEN:
                window.getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                break;
            case SYSTEM_UI_MODE_LIGHT_BAR:
                if (VERSION.SDK_INT >= VERSION_CODES.M) {
                    window.getDecorView()
                            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
                break;
        }
    }

}
