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
import me.zhixingye.base.component.BasicActivity;
import me.zhixingye.base.view.SegmentedControlView;
import me.zhixingye.salty.R;
import me.zhixingye.salty.util.AndroidHelper;

public class PushMessageActivity extends BasicActivity {

    private static final String[] TITLE = {"好友通知", "系统消息"};

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, PushMessageActivity.class);
        context.startActivity(intent);
    }

    private SegmentedControlView mSegmentedControlView;
    private ContactOperationListFragment mContactOperationListFragment;
    private SystemMessageFragment mSystemMessageFragment;
    private FragmentManager mFragmentManager;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_push_message;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mSegmentedControlView = new SegmentedControlView(this);
        mContactOperationListFragment = new ContactOperationListFragment();
        mSystemMessageFragment = new SystemMessageFragment();
        mFragmentManager = getSupportFragmentManager();
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        setToolbarId(R.id.mDefaultToolbar,true);
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

        getToolbar().addView(mSegmentedControlView, new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));

        mFragmentManager.beginTransaction()
                .add(R.id.mFlContent, mContactOperationListFragment)
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
                            .show(mContactOperationListFragment)
                            .hide(mSystemMessageFragment)
                            .commit();
                    break;
                case 1:
                    mFragmentManager.beginTransaction()
                            .show(mSystemMessageFragment)
                            .hide(mContactOperationListFragment)
                            .commit();
                    break;
            }
        }
    };
}
