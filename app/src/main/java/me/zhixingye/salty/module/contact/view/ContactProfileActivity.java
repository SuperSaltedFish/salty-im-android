package me.zhixingye.salty.module.contact.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import me.zhixingye.base.component.mvp.MVPActivity;
import me.zhixingye.base.view.FlowLayout;
import me.zhixingye.base.view.PageIndicator;
import me.zhixingye.salty.R;
import me.zhixingye.salty.widget.adapter.CenterCropImagePagerAdapter;


public class ContactProfileActivity
        extends MVPActivity {

    private static final String INTENT_EXTRA_CONTACT_ID = "ContactID";

    public static void startActivity(Context context, String contactID) {
        Intent intent = new Intent(context, ContactProfileActivity.class);
        intent.putExtra(INTENT_EXTRA_CONTACT_ID, contactID);
        context.startActivity(intent);
    }

    private FloatingActionButton mBtnStartChat;
    private ImageView mIvAvatar;
    private ImageView mIvSexIcon;
    private TextView mTvTitle;
    private TextView mTvSignature;
    private TextView mTvNickname;
    private TextView mTvLocationAndAge;
    private TextView mTvLastLabel;
    private AppBarLayout mAppBarLayout;
    private FlowLayout mLabelFlowLayout;
    private TabLayout mTabLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private PageIndicator mPageIndicator;
    private ViewPager mVpBanner;
    private ViewPager mVpContactInfo;

    private CenterCropImagePagerAdapter mCropImagePagerAdapter;

    private ArrayList<Object> mPicUrlList;
    private String mContactID;


    @Override
    protected int getLayoutID() {
        return R.layout.activity_contact_profile;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mAppBarLayout = findViewById(R.id.mAppBarLayout);
        mBtnStartChat = findViewById(R.id.mBtnStartChat);
        mTvNickname = findViewById(R.id.mTvNickname);
        mTvSignature = findViewById(R.id.mTvSignature);
        mIvAvatar = findViewById(R.id.mIvAvatar);
        mIvSexIcon = findViewById(R.id.mIvSexIcon);
        mTvTitle = findViewById(R.id.mTvTitle);
        mTvLocationAndAge = findViewById(R.id.mTvLocationAndAge);
        mLabelFlowLayout = findViewById(R.id.mLabelFlowLayout);
        mTabLayout = findViewById(R.id.mTabLayout);
        mPageIndicator = findViewById(R.id.mPageIndicator);
        mVpBanner = findViewById(R.id.mVpBanner);
        mVpContactInfo = findViewById(R.id.mVpContactInfo);
        mCollapsingToolbarLayout = findViewById(R.id.mCollapsingToolbarLayout);
        mTvLastLabel = (TextView) getLayoutInflater().inflate(R.layout.item_label_normal, mLabelFlowLayout, false);
        mContactID = getIntent().getStringExtra(INTENT_EXTRA_CONTACT_ID);
        mPicUrlList = new ArrayList<>(6);
        mCropImagePagerAdapter = new CenterCropImagePagerAdapter(mPicUrlList);
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
//        if (TextUtils.isEmpty(mContactID)) {
//            finish();
//            return;
//        }
//        setSystemUiMode(SYSTEM_UI_MODE_TRANSPARENT_BAR_STATUS);
//        setToolbarId(R.id.mDefaultToolbar,true);
//        setTitle(null);
//
//        fillTestData();
//
//        mPageIndicator.setIndicatorColorSelected(Color.WHITE);
//        mPageIndicator.setIndicatorColorUnselected(ContextCompat.getColor(this, R.color.backgroundColorWhiteLight));
//        mPageIndicator.setIndicatorRadius((int) AndroidHelper.dip2px(3));
//        mPageIndicator.setupWithViewPager(mVpBanner);
//
//        mVpBanner.setAdapter(mCropImagePagerAdapter);
//        mVpContactInfo.setAdapter(new ContactInfoPagerAdapter(getSupportFragmentManager(), mContactID, getResources().getStringArray(R.array.ContactProfile_TabTitle)));
//
//        mTabLayout.setupWithViewPager(mVpContactInfo);
//
//        mLabelFlowLayout.setLineSpace((int) AndroidHelper.dip2px(8));
//        mLabelFlowLayout.setItemSpace((int) AndroidHelper.dip2px(8));
//
//        int size = (int) AndroidHelper.dip2px(10);
//        Drawable drawable = getDrawable(R.drawable.ic_add);
//        if (drawable != null) {
//            drawable.setTint(Color.WHITE);
//            drawable.setBounds(0, 0, size, size);
//        }
//        mTvLastLabel.setText(R.string.EditContactLabelActivity_Title);
//        mTvLastLabel.setCompoundDrawables(null, null, drawable, null);
//        mTvLastLabel.setPadding(size, mTvLastLabel.getPaddingTop(), size * 2 / 3, mTvLastLabel.getPaddingBottom());
//        mTvLastLabel.setCompoundDrawablePadding(size / 4);
//
//        mTvTitle.setAlpha(0);
//        mBtnStartChat.setOnClickListener(mOnViewClickListener);
//        mLabelFlowLayout.setOnClickListener(mOnViewClickListener);
//        mAppBarLayout.addOnOffsetChangedListener(mOnOffsetChangedListener);
//        mPresenter.init(mContactID);
    }

//
//    private void fillTestData() {
//        mPicUrlList.add(R.drawable.temp_image_1);
//        mPicUrlList.add(R.drawable.temp_image_2);
//        mPicUrlList.add(R.drawable.temp_image_3);
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_contact_profile, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.ContactMenu_ReviseRemarks:
//                startRemarkInfoActivity();
//                break;
//            case R.id.ContactMenu_DeleteContact:
//                mPresenter.deleteContact();
//                break;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//        return true;
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == EditContactTagsActivity.RESULT_CODE) {
//            ContactEntity contact = mPresenter.getContact();
//            ArrayList<String> newTags = data.getStringArrayListExtra(EditContactTagsActivity.INTENT_EXTRA_LABEL);
//            ArrayList<String> oldTags = contact.getTags();
//            if (!StringUtil.isEquals(newTags, oldTags, true)) {
//                contact.setTags(newTags);
//                mPresenter.saveRemarkInfo(contact);
//            }
//        }
//    }
//
//    private void startRemarkInfoActivity() {
//        RemarkInfoActivity.startActivityForResult(this, mPresenter.getContact());
//    }
//
//    private void startChatActivity() {
//        ChatActivity.startActivity(this, mContactID, Conversation.ConversationType.PRIVATE);
//        finish();
//    }
//
//    private void startEditContactLabelActivity() {
//        Intent intent = new Intent(com.yzx.chat.module.contact.view.ContactProfileActivity.this, EditContactTagsActivity.class);
//        intent.putExtra(EditContactTagsActivity.INTENT_EXTRA_LABEL, mPresenter.getContact().getTags());
//        intent.putExtra(EditContactTagsActivity.INTENT_EXTRA_SELECTABLE_LABEL, mPresenter.getAllTags());
//        startActivityForResult(intent, 0);
//    }
//
//    private final View.OnClickListener mOnViewClickListener = new OnOnlySingleClickListener() {
//        @Override
//        public void onSingleClick(View v) {
//            switch (v.getId()) {
//                case R.id.mIvStartChat:
//                    startChatActivity();
//                    break;
//                case R.id.mLabelFlowLayout:
//                    startEditContactLabelActivity();
//                    break;
//
//            }
//        }
//    };
//
//    private AppBarLayout.OnOffsetChangedListener mOnOffsetChangedListener = new AppBarLayout.OnOffsetChangedListener() {
//        private boolean isHide;
//
//        @Override
//        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//            if (mCollapsingToolbarLayout.getHeight() + verticalOffset < mCollapsingToolbarLayout.getScrimVisibleHeightTrigger()) {
//                if (!isHide) {
//                    isHide = true;
//                    mIvAvatar.animate().alpha(0).scaleX(0).scaleY(0).setDuration(200);
//                    mTvTitle.animate().alpha(1).setDuration(200);
//                }
//            } else {
//                if (isHide) {
//                    isHide = false;
//                    mIvAvatar.animate().alpha(1).scaleX(1).scaleY(1).setDuration(200);
//                    mTvTitle.animate().alpha(0).setDuration(200);
//                }
//            }
//        }
//    };
//
//    @Override
//    public ContactProfileContract.Presenter getPresenter() {
//        return new ContactProfilePresenter();
//    }
//
//    @Override
//    public void updateContactInfo(ContactEntity contact) {
//        mTvTitle.setText(contact.getName());
//        mTvNickname.setText(contact.getUserProfile().getNickname());
//        UserEntity user = contact.getUserProfile();
//        mIvSexIcon.setSelected(user.getSex() == UserEntity.SEX_WOMAN);
//        StringBuilder locationAndAge = new StringBuilder();
//        locationAndAge.append(user.getAge());
//        if (!TextUtils.isEmpty(user.getLocation())) {
//            locationAndAge.append(" · ").append(user.getLocation());
//        }
//        mTvLocationAndAge.setText(locationAndAge.toString());
//        if (TextUtils.isEmpty(user.getSignature())) {
//            mTvSignature.setText(null);
//            mTvSignature.setVisibility(View.GONE);
//        } else {
//            mTvSignature.setText(user.getSignature());
//            mTvSignature.setVisibility(View.VISIBLE);
//        }
//        GlideUtil.loadAvatarFromUrl(this, mIvAvatar, user.getAvatar());
//        mLabelFlowLayout.removeAllViews();
//
//        List<String> tags = contact.getTags();
//        if (tags != null && tags.size() != 0) {
//            for (String tag : tags) {
//                TextView label = (TextView) getLayoutInflater().inflate(R.layout.item_label_normal, mLabelFlowLayout, false);
//                label.setText(tag);
//                mLabelFlowLayout.addView(label);
//            }
//        }
//        mLabelFlowLayout.addView(mTvLastLabel);
//    }
//
//    @Override
//    public void goBack() {
//        finish();
//    }
//

}
