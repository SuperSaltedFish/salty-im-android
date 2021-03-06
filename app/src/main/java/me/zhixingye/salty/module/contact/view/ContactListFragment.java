package me.zhixingye.salty.module.contact.view;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.salty.protos.ContactProfile;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import me.zhixingye.base.adapter.BasicListAdapterAdapter;
import me.zhixingye.base.component.mvvm.MVVMFragment;
import me.zhixingye.base.view.IndexBarView;
import me.zhixingye.salty.R;
import me.zhixingye.salty.module.contact.viewmodel.ContactListViewModel;
import me.zhixingye.salty.util.AndroidHelper;
import me.zhixingye.salty.util.AnimationUtil;
import me.zhixingye.salty.widget.adapter.ContactListAdapter;
import me.zhixingye.salty.widget.adapter.ContactListHeaderAdapter;
import me.zhixingye.salty.widget.adapter.holder.ContactListHeaderHolder;
import me.zhixingye.salty.widget.view.LetterSegmentationItemDecoration;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月17日.
 */
public class ContactListFragment extends MVVMFragment {

    private Toolbar mDefaultToolbar;
    private RecyclerView mRvContact;
    private IndexBarView mIndexBarView;
    private TextView mTvIndexBarHint;
    private FloatingActionButton mFBtnAdd;
    private SwipeRefreshLayout mSrlContact;

    private ContactListHeaderAdapter mContactListHeaderAdapter;
    private ContactListAdapter mContactListAdapter;

    private LetterSegmentationItemDecoration mLetterSegmentation;
    private LinearLayoutManager mLinearLayoutManager;

    private ContactListViewModel mContactListViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_contact_list;
    }

    @Override
    protected void init(View rootView) {
        mDefaultToolbar = rootView.findViewById(R.id.mDefaultToolbar);
        mRvContact = rootView.findViewById(R.id.mRvContact);
        mIndexBarView = rootView.findViewById(R.id.mIndexBarView);
        mTvIndexBarHint = rootView.findViewById(R.id.mTvIndexBarHint);
        mFBtnAdd = rootView.findViewById(R.id.mFBtnAdd);
        mSrlContact = rootView.findViewById(R.id.mSrlContact);

        mContactListAdapter = new ContactListAdapter();
        mContactListHeaderAdapter = new ContactListHeaderAdapter();
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        mDefaultToolbar.setTitle(R.string.app_name);

        setupRecyclerViewAndAdapter();
        setupIndexBar();
        setupFloatButton();
        setupViewModule();

        loadData();
    }

    private void setupRecyclerViewAndAdapter() {
        mLetterSegmentation = new LetterSegmentationItemDecoration();
        mLetterSegmentation.setStartDrawX(AndroidHelper.dip2px(16));
        mLetterSegmentation.setBackgroundColor(ContextCompat.getColor(mContext, R.color.backgroundColorWhiteLight));
        mLetterSegmentation.setTextColor(AndroidHelper.getThemeColor(mContext, android.R.attr.textColorPrimary, Color.GRAY));
        mLetterSegmentation.setTextSize(AndroidHelper.sp2px(15));

        mLinearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        mRvContact.setLayoutManager(mLinearLayoutManager);
        mRvContact.setHasFixedSize(true);
        mRvContact.addItemDecoration(mLetterSegmentation);

        mRvContact.setAdapter(new ConcatAdapter(mContactListHeaderAdapter, mContactListAdapter));

        mContactListHeaderAdapter.setOnItemClickListener(new BasicListAdapterAdapter.SimpleOnItemClickListener<ContactListHeaderHolder.Type>() {
            @Override
            public void onClick(int position, ContactListHeaderHolder.Type type) {
                switch (type) {
                    case SYSTEM_NOTIFICATION:
                        PushMessageActivity.startActivity(mContext);
                        break;
                    case GROUP:
                        break;
                    case CONTACT_LABEL:
                        break;
                    default:
                        return;

                }
            }
        });

        mContactListAdapter.setOnItemClickListener(new BasicListAdapterAdapter.SimpleOnItemClickListener<ContactProfile>() {
            @Override
            public void onClick(int position, ContactProfile data) {
                ContactProfileActivity.startActivity(mContext, data.getUserProfile().getUserId());
            }
        });
    }

    private void setupIndexBar() {
        mIndexBarView.setSelectedTextColor(AndroidHelper.getThemeColor(mContext, android.R.attr.colorAccent));
        mIndexBarView.setOnTouchSelectedListener(new IndexBarView.OnTouchSelectedListener() {
            @Override
            public void onSelected(int position, String sortKey) {
                int scrollPosition = mLetterSegmentation.findPositionByLetter(sortKey);
                if (scrollPosition >= 0) {
                    mLinearLayoutManager.scrollToPositionWithOffset(scrollPosition, 0);
                }
                if (mFBtnAdd.getTag() == null) {
                    AnimationUtil.scaleAnim(mFBtnAdd, 0, 0, 300);
                    AnimationUtil.scaleAnim(mTvIndexBarHint, 1f, 1f, 300);
                    mFBtnAdd.setTag(true);
                }
                mTvIndexBarHint.setText(sortKey);

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

    private void setupFloatButton() {
        mFBtnAdd.setOnClickListener(mOnClickListener);
    }

    private void setupViewModule() {
        mContactListViewModel = createViewModel(ContactListViewModel.class);
        mContactListViewModel.getContactListData().observe(this, new Observer<List<ContactProfile>>() {
            @Override
            public void onChanged(List<ContactProfile> contactProfiles) {
                mContactListAdapter.submitList(contactProfiles);
            }
        });

        mContactListViewModel.getContactListLoadingStateData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean data) {
                boolean isDisplay = Boolean.TRUE.equals(data);
                mSrlContact.setEnabled(isDisplay);
                mSrlContact.setRefreshing(isDisplay);
            }
        });
    }

    private void loadData() {
        mContactListViewModel.loadAllContact();
    }

    private void startFindNewContactActivity() {
        FindNewContactActivity.startActivity(mContext);
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mFBtnAdd:
                    startFindNewContactActivity();
                    break;
            }
        }
    };

}
