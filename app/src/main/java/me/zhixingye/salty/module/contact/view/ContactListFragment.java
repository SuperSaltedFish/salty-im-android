package me.zhixingye.salty.module.contact.view;

import android.graphics.Color;
import android.graphics.Outline;
import android.os.Bundle;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.salty.protos.ContactProfile;
import com.salty.protos.ContactRemark;
import com.salty.protos.UserProfile;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.zhixingye.salty.R;
import me.zhixingye.salty.basic.BasicFragment;
import me.zhixingye.salty.util.AndroidHelper;
import me.zhixingye.salty.util.AnimationUtil;
import me.zhixingye.salty.widget.adapter.ContactListAdapter;
import me.zhixingye.salty.widget.adapter.ContactListHeaderAdapter;
import me.zhixingye.salty.widget.view.IndexBarView;
import me.zhixingye.salty.widget.view.LetterSegmentationItemDecoration;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月17日.
 */
public class ContactListFragment extends BasicFragment {

    private Toolbar mDefaultToolbar;
    private RecyclerView mRvContact;
    private IndexBarView mIndexBarView;
    private TextView mTvIndexBarHint;
    private FloatingActionButton mFBtnAdd;

    private ContactListHeaderAdapter mContactListHeaderAdapter;
    private ContactListAdapter mContactListAdapter;

    private LetterSegmentationItemDecoration mLetterSegmentation;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_contact_list;
    }

    @Override
    protected void init(View parentView) {
        mDefaultToolbar = parentView.findViewById(R.id.mDefaultToolbar);
        mRvContact = parentView.findViewById(R.id.mRvContact);
        mIndexBarView = parentView.findViewById(R.id.mIndexBarView);
        mTvIndexBarHint = parentView.findViewById(R.id.mTvIndexBarHint);
        mFBtnAdd = parentView.findViewById(R.id.mFBtnAdd);

        mContactListAdapter = new ContactListAdapter();
        mContactListHeaderAdapter = new ContactListHeaderAdapter();
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        mDefaultToolbar.setTitle(R.string.app_name);

        setupRecyclerViewAndAdapter();
        setupIndexBar();

        setMockData();
    }

    private void setMockData() {
        List<ContactProfile> profiles = new ArrayList<>();
        UserProfile user = UserProfile.newBuilder()
                .setUserId("123")
                .setNickname("叶智星")
                .setBirthday(System.currentTimeMillis() / 3 * 2)
                .setDescription("每个不曾起舞的日子都是对生命的辜负。")
                .setSex(UserProfile.Sex.MALE)
                .build();
        ContactRemark remark = ContactRemark.newBuilder()
                .addTags("同学")
                .build();
        ContactProfile profile1 = ContactProfile.newBuilder()
                .setRemarkInfo(remark)
                .setUserProfile(user)
                .setSortKey("A")
                .build();

        ContactProfile profile2 = ContactProfile.newBuilder()
                .setRemarkInfo(remark)
                .setUserProfile(user)
                .setSortKey("B")
                .build();

        ContactProfile profile3 = ContactProfile.newBuilder()
                .setRemarkInfo(remark)
                .setUserProfile(user)
                .setSortKey("C")
                .build();

        ContactProfile profile4 = ContactProfile.newBuilder()
                .setRemarkInfo(remark)
                .setUserProfile(user)
                .setSortKey("D")
                .build();

        ContactProfile profile5 = ContactProfile.newBuilder()
                .setRemarkInfo(remark)
                .setUserProfile(user)
                .setSortKey("E")
                .build();

        ContactProfile profile6 = ContactProfile.newBuilder()
                .setRemarkInfo(remark)
                .setUserProfile(user)
                .setSortKey("F")
                .build();

        ContactProfile profile7 = ContactProfile.newBuilder()
                .setRemarkInfo(remark)
                .setUserProfile(user)
                .setSortKey("G")
                .build();

        ContactProfile profile8 = ContactProfile.newBuilder()
                .setRemarkInfo(remark)
                .setUserProfile(user)
                .setSortKey("H")
                .build();

        ContactProfile profile9 = ContactProfile.newBuilder()
                .setRemarkInfo(remark)
                .setUserProfile(user)
                .setSortKey("I")
                .build();

        ContactProfile profile10 = ContactProfile.newBuilder()
                .setRemarkInfo(remark)
                .setUserProfile(user)
                .setSortKey("J")
                .build();


        profiles.add(profile1);
        profiles.add(profile1);
        profiles.add(profile2);
        profiles.add(profile3);
        profiles.add(profile3);
        profiles.add(profile3);
        profiles.add(profile3);
        profiles.add(profile4);
        profiles.add(profile5);
        profiles.add(profile5);
        profiles.add(profile5);
        profiles.add(profile6);
        profiles.add(profile7);
        profiles.add(profile7);
        profiles.add(profile8);
        profiles.add(profile9);
        profiles.add(profile10);

        mContactListAdapter.submitList(profiles);

    }

    private void setupRecyclerViewAndAdapter() {
        mLetterSegmentation = new LetterSegmentationItemDecoration();
        mLetterSegmentation.setStartDrawX(AndroidHelper.dip2px(16));
        mLetterSegmentation.setBackgroundColor(ContextCompat.getColor(mContext, R.color.backgroundColorWhiteLight));
        mLetterSegmentation.setTextColor(AndroidHelper.getThemeColor(mContext, android.R.attr.textColorSecondary, Color.GRAY));
        mLetterSegmentation.setTextSize(AndroidHelper.sp2px(15));

        mLinearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        mRvContact.setLayoutManager(mLinearLayoutManager);
        mRvContact.setHasFixedSize(true);
        mRvContact.addItemDecoration(mLetterSegmentation);

        mRvContact.setAdapter(new ConcatAdapter(mContactListHeaderAdapter, mContactListAdapter));
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

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
