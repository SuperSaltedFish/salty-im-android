package me.zhixingye.salty.module.contact.view;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.MergeAdapter;
import androidx.recyclerview.widget.RecyclerView;
import me.zhixingye.salty.R;
import me.zhixingye.salty.basic.BasicFragment;
import me.zhixingye.salty.util.AndroidHelper;
import me.zhixingye.salty.util.AnimationUtil;
import me.zhixingye.salty.widget.adapter.ContactAdapter;
import me.zhixingye.salty.widget.listener.ImageAutoLoadScrollListener;
import me.zhixingye.salty.widget.view.IndexBarView;
import me.zhixingye.salty.widget.view.LetterSegmentationItemDecoration;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月17日.
 */
public class ContactListFragment extends BasicFragment {
    @Override
    protected int getLayoutID() {
        return R.layout.fragment_contact_list;
    }

    private Toolbar mDefaultToolbar;
    private RecyclerView mRvContact;
    private IndexBarView mIndexBarView;
    private TextView mTvIndexBarHint;
    private FloatingActionButton mFBtnAdd;

    private ContactAdapter mContactAdapter;

    private LinearLayoutManager mLinearLayoutManager;
    private LetterSegmentationItemDecoration mLetterSegmentation;

    @Override
    protected void init(View parentView) {
        mDefaultToolbar = parentView.findViewById(R.id.mDefaultToolbar);
        mRvContact = parentView.findViewById(R.id.mRvContact);
        mIndexBarView = parentView.findViewById(R.id.mIndexBarView);
        mTvIndexBarHint = parentView.findViewById(R.id.mTvIndexBarHint);
        mFBtnAdd = parentView.findViewById(R.id.mFBtnAdd);

        mContactAdapter = new ContactAdapter();
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        mDefaultToolbar.setTitle(R.string.app_name);

        mLetterSegmentation = new LetterSegmentationItemDecoration();
        mLetterSegmentation.setLineColor(ContextCompat.getColor(mContext, R.color.dividerColor));
        mLetterSegmentation.setLineWidth((int) AndroidHelper.dip2px(1));
        mLetterSegmentation.setTextColor(AndroidHelper.getThemeColor(mContext, android.R.attr.textColorSecondary, Color.GRAY));
        mLetterSegmentation.setTextSize(AndroidHelper.sp2px(15));

        mLinearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        mRvContact.setLayoutManager(mLinearLayoutManager);
        mRvContact.setAdapter(mContactAdapter);
        mRvContact.setHasFixedSize(true);
        mRvContact.addItemDecoration(mLetterSegmentation);
        mRvContact.addOnScrollListener(new ImageAutoLoadScrollListener());

        setupIndexBar();
    }

    private void setupIndexBar() {
        MergeAdapter
        mIndexBarView.setSelectedTextColor(AndroidHelper.getThemeColor(mContext, android.R.attr.textColorSecondary, Color.GRAY));
        mIndexBarView.setOnTouchSelectedListener(new IndexBarView.OnTouchSelectedListener() {
            @Override
            public void onSelected(int position, String text) {
                final int scrollPosition = mContactAdapter.findPositionByLetter(text);
                mLinearLayoutManager.scrollToPositionWithOffset(scrollPosition, 0);
                if (mFBtnAdd.getTag() == null) {
                    AnimationUtil.scaleAnim(mFBtnAdd, 0, 0, 300);
                    AnimationUtil.scaleAnim(mTvIndexBarHint, 1f, 1f, 300);
                    mFBtnAdd.setTag(true);
                }
                mTvIndexBarHint.setText(text);

            }

            @Override
            public void onCancelSelected() {
                mFBtnAdd.setTag(null);
                AnimationUtil.scaleAnim(mTvIndexBarHint, 0, 0, 250);
                AnimationUtil.scaleAnim(mFBtnAdd, 1f, 1f, 250);
            }

            @Override
            public void onMove(int offsetPixelsY) {
                int startOffset = mTvIndexBarHint.getHeight() / 2;
                if (startOffset > offsetPixelsY) {
                    mTvIndexBarHint.setTranslationY(0);
                } else if (offsetPixelsY > mIndexBarView.getHeight() - startOffset) {
                    mTvIndexBarHint.setTranslationY(mIndexBarView.getHeight() - startOffset * 2);
                } else {
                    mTvIndexBarHint.setTranslationY(offsetPixelsY - startOffset);
                }
            }
        });
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
