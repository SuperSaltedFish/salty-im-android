package me.zhixingye.salty.module.contact.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import me.zhixingye.salty.R;
import me.zhixingye.salty.basic.BasicCompatActivity;
import me.zhixingye.salty.util.AndroidHelper;
import me.zhixingye.salty.widget.view.SegmentedControlView;

public class PushMessageActivity extends BasicCompatActivity {

    private static final String[] TITLE = {"好友通知", "系统消息"};

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, PushMessageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    private SegmentedControlView mSegmentedControlView;
    private ContactPushMessageFragment mContactPushMessageFragment;
    private SystemMessageFragment mSystemMessageFragment;
    private FragmentManager mFragmentManager;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_push_message;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mSegmentedControlView = new SegmentedControlView(this);
        mContactPushMessageFragment = new ContactPushMessageFragment();
        mSystemMessageFragment = new SystemMessageFragment();
        mFragmentManager = getSupportFragmentManager();
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        setDisplayHomeAsUpEnabled(true);
        setTitle(null);

        int paddingLeftRight = (int) AndroidHelper.dip2px(10);
        int paddingTopBottom = (int) AndroidHelper.dip2px(5);
        mSegmentedControlView
                .setItems(TITLE)
                .setColors(Color.WHITE, ContextCompat.getColor(this, R.color.colorAccent))
                .setTextSize(AndroidHelper.sp2px(14))
                .setItemPadding(paddingLeftRight, paddingTopBottom, paddingLeftRight, paddingTopBottom)
                .setRadius(AndroidHelper.dip2px(4))
                .setDefaultSelectedPosition(0)
                .setOnSelectionChangedListener(mOnSelectedChangedListener)
                .update();

        mDefaultToolbar.addView(mSegmentedControlView, new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));

        mFragmentManager.beginTransaction()
                .add(R.id.mFlContent, mContactPushMessageFragment)
                .add(R.id.mFlContent, mSystemMessageFragment)
                .hide(mSystemMessageFragment)
                .commit();
    }

    private final SegmentedControlView.OnSelectedChangedListener mOnSelectedChangedListener = new SegmentedControlView.OnSelectedChangedListener() {
        @Override
        public void onSelected(int position, String text) {
            switch (position) {
                case 0:
                    mFragmentManager.beginTransaction()
                            .show(mContactPushMessageFragment)
                            .hide(mSystemMessageFragment)
                            .commit();
                    break;
                case 1:
                    mFragmentManager.beginTransaction()
                            .show(mSystemMessageFragment)
                            .hide(mContactPushMessageFragment)
                            .commit();
                    break;
            }
        }
    };
}
