package me.zhixingye.salty.module.splash.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import me.zhixingye.base.component.mvp.MVPActivity;
import me.zhixingye.base.component.mvvm.MVVMActivity;
import me.zhixingye.base.listener.OnOnlySingleClickListener;
import me.zhixingye.base.view.PageIndicator;
import me.zhixingye.salty.R;
import me.zhixingye.salty.configure.AppConfig;
import me.zhixingye.salty.module.login.view.LoginActivity;
import me.zhixingye.salty.module.splash.viewmodel.GuideViewModel;
import me.zhixingye.salty.widget.adapter.GuidePagerAdapter;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class GuideActivity extends MVVMActivity {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, GuideActivity.class));
    }

    private ViewPager mGuideViewPager;
    private PageIndicator mPageIndicator;
    private TextSwitcher mTextSwitcher;
    private LinearLayout mLlTextIndicator;

    private GuidePagerAdapter mGuidePagerAdapter;

    private GuideViewModel mGuideViewModel;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_guide;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mGuideViewPager = findViewById(R.id.mGuideViewPager);
        mPageIndicator = findViewById(R.id.mPageIndicator);
        mTextSwitcher = findViewById(R.id.mTextSwitcher);
        mLlTextIndicator = findViewById(R.id.mLlTextIndicator);
        mGuidePagerAdapter = new GuidePagerAdapter(this);
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        setSystemUiMode(SYSTEM_UI_MODE_TRANSPARENT_LIGHT_BAR_STATUS_AND_NAVIGATION);

        mGuideViewPager.setAdapter(mGuidePagerAdapter);
        mGuideViewPager.addOnPageChangeListener(mOnPageChangeListener);

        mPageIndicator.setIndicatorColorSelected(ContextCompat.getColor(this, R.color.colorAccent));
        mPageIndicator.setIndicatorColorUnselected(ContextCompat.getColor(this, R.color.colorAccentLight));
        mPageIndicator.setupWithViewPager(mGuideViewPager);

        mLlTextIndicator.setOnClickListener(mOnNextClickListener);

        setupViewModel();
    }

    private void setupViewModel() {
        mGuideViewModel = createViewModel(GuideViewModel.class);
    }

    private void startLoginActivity() {
        mGuideViewModel.setEverStartedGuide(true);
        LoginActivity.startActivity(this);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private final View.OnClickListener mOnNextClickListener = new OnOnlySingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            int currentIndex = mGuideViewPager.getCurrentItem();
            if (currentIndex < mGuidePagerAdapter.getCount() - 1) {
                mGuideViewPager.setCurrentItem(currentIndex + 1);
            } else {
                startLoginActivity();
            }
        }
    };

    private final ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {

        private boolean isSlideToEnd;
        private boolean isAlreadyStart;
        private int mLastPosition;

        @Override
        public void onPageSelected(int position) {
            int count = mGuidePagerAdapter.getCount();
            if (position == count - 1 && mLastPosition < count - 1) {
                mTextSwitcher.showNext();
            } else if (mLastPosition == count - 1 && position < count - 1) {
                mTextSwitcher.showPrevious();
            }
            mLastPosition = position;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int count = mGuidePagerAdapter.getCount();
            if (!isAlreadyStart && isSlideToEnd && position == count - 1) {
                startLoginActivity();
                isAlreadyStart = true;
            } else isSlideToEnd = (position == count - 1);
        }
    };

}
