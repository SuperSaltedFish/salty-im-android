package me.zhixingye.salty.module.main.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import me.zhixingye.base.component.mvp.MVPBasicActivity;
import me.zhixingye.base.view.BottomTabLayout;
import me.zhixingye.im.sdk.IMClient;
import me.zhixingye.salty.R;
import me.zhixingye.salty.module.chat.view.ConversationFragment;
import me.zhixingye.salty.module.contact.view.ContactListFragment;
import me.zhixingye.salty.module.moment.view.MomentsFragment;
import me.zhixingye.salty.module.profile.view.ProfileFragment;
import me.zhixingye.salty.util.AndroidHelper;
import me.zhixingye.salty.util.AnimationUtil;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class MainActivity extends MVPBasicActivity {

    private static final String TAG = "MainActivity";

    private static final String EXTRA_TRANSITION_DRAWABLE = "TransitionDrawable";
    private static final String EXTRA_TRANSITION_COLOR = "TransitionColor";

    public static void startActivity(Activity activity) {
        activity.startActivity(new Intent(activity, MainActivity.class));
    }

    public static void startActivityByImageTransition(Activity activity, int drawableRes) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(EXTRA_TRANSITION_DRAWABLE, drawableRes);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent, null);
        activity.overridePendingTransition(0, 0);
    }

    public static void startActivityByColorTransition(Activity activity, int colorRes) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(EXTRA_TRANSITION_COLOR, colorRes);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent, null);
        activity.overridePendingTransition(0, 0);
    }

    private BottomTabLayout mBottomTabLayout;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mBottomTabLayout = findViewById(R.id.mBottomTabLayout);
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        setSystemUiMode(SYSTEM_UI_MODE_TRANSPARENT_BAR_STATUS);
        getWindow().setBackgroundDrawable(null);

        initFragment();
        initBottomTab();
        initEnterTransition();

    }

    private void initBottomTab() {
        mBottomTabLayout
                .addTab(R.drawable.ic_conversation_focus, R.drawable.ic_conversation_unfocus, "聊天")
                .addTab(R.drawable.ic_contacts_focus, R.drawable.ic_contacts_unfocus, "联系人")
                .addTab(R.drawable.ic_moments_focus, R.drawable.ic_moments_unfocus, "动态")
                .addTab(R.drawable.ic_personal_focus, R.drawable.ic_personal_unfocus, "我的")
                .setTitleTextSize(AndroidHelper.sp2px(11))
                .addOnTabItemSelectedListener(new BottomTabLayout.OnTabItemSelectedListener() {
                    @Override
                    public void onSelected(int position) {
                        selectFragment(position);
                    }

                    @Override
                    public void onRepeated(int position) {

                    }
                })
                .setSelectPosition(0, false, false);
        mBottomTabLayout.setSelectPosition(0, false, false);
    }

    private void initFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        addFragment(transaction, new ConversationFragment(), "0", true);
        addFragment(transaction, new ContactListFragment(), "1", false);
        addFragment(transaction, new MomentsFragment(), "2", false);
        addFragment(transaction, new ProfileFragment(), "3", false);
        transaction.commitNowAllowingStateLoss();
    }

    private void addFragment(FragmentTransaction transaction, Fragment fragment, String tag, boolean isShow) {
        Fragment old = getSupportFragmentManager().findFragmentByTag(tag);
        if (old == null) {
            old = fragment;
            transaction.add(R.id.mClContent, fragment, tag);
        }
        if (!isShow) {
            transaction.hide(old);
        }
    }

    private void selectFragment(int index) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        for (Fragment fragment : manager.getFragments()) {
            if (TextUtils.equals(fragment.getTag(), String.valueOf(index))) {
                if (!fragment.isVisible()) {
                    transaction.show(fragment);
                }
            } else {
                if (fragment.isVisible()) {
                    transaction.hide(fragment);
                }
            }
        }
        transaction.commit();
    }

    private void initEnterTransition() {
        int drawableRes = getIntent().getIntExtra(EXTRA_TRANSITION_DRAWABLE, -1);
        int colorRes = getIntent().getIntExtra(EXTRA_TRANSITION_COLOR, -1);
        Drawable image = null;
        if (drawableRes != -1 || colorRes != -1) {
            if (drawableRes != -1) {
                image = ContextCompat.getDrawable(this, drawableRes);
            } else {
                image = new ColorDrawable(ContextCompat.getColor(this, colorRes));
            }
        }
        if (image != null) {
            AnimationUtil.circularRevealHideByFullActivityAnim(this, image);
        }
    }


    public void onClick(View v) {
        IMClient.get().getAccountService().logout();
        finish();
    }
}
