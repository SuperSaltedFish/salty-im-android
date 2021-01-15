package me.zhixingye.salty.module.contact.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.salty.protos.ContactOperationMessage;
import com.salty.protos.UserProfile;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import me.zhixingye.base.component.mvp.MVPBasicActivity;
import me.zhixingye.base.listener.OnOnlySingleClickListener;
import me.zhixingye.base.view.PageIndicator;
import me.zhixingye.im.IMCore;
import me.zhixingye.im.sdk.IMClient;
import me.zhixingye.salty.R;
import me.zhixingye.salty.module.contact.contract.StrangerProfileContract;
import me.zhixingye.salty.tool.UserDataFormatter;
import me.zhixingye.salty.util.AndroidHelper;
import me.zhixingye.salty.util.GlideUtil;
import me.zhixingye.salty.widget.adapter.CenterCropImagePagerAdapter;

/**
 * Created by YZX on 2018年01月29日.
 * 优秀的代码是它自己最好的文档,当你考虑要添加一个注释时,问问自己:"如何能改进这段代码，以让它不需要注释？"
 */
public class StrangerProfileActivity
        extends MVPBasicActivity
        implements StrangerProfileContract.View {

    private static final String INTENT_EXTRA_USER_ID = "UserId";

    public static void startActivityByUserId(Context context, String userId) {
        Intent intent = new Intent(context, StrangerProfileActivity.class);
        intent.putExtra(StrangerProfileActivity.INTENT_EXTRA_USER_ID, userId);
        context.startActivity(intent);
    }

    private EditText mEtReason;
    private Button mBtnConfirm;
    private ViewPager mVpBanner;
    private PageIndicator mPageIndicator;
    private TextView mTvSignature;
    private TextView mTvNickname;
    private TextView mTvLocationAndAge;
    private ImageView mIvSexIcon;
    private ImageView mIvAvatar;
    private CenterCropImagePagerAdapter mCropImagePagerAdapter;

    private ArrayList<Object> mPicUrlList;

    private UserProfile mUserProfile;
    private ContactOperationMessage mContactOperationMessage;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_stranger_profile;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mEtReason = findViewById(R.id.mEtReason);
        mBtnConfirm = findViewById(R.id.mBtnConfirm);
        mTvNickname = findViewById(R.id.mTvNickname);
        mTvSignature = findViewById(R.id.mTvSignature);
        mIvSexIcon = findViewById(R.id.mIvSexIcon);
        mIvAvatar = findViewById(R.id.mIvAvatar);
        mTvLocationAndAge = findViewById(R.id.mTvLocationAndAge);
        mPageIndicator = findViewById(R.id.mPageIndicator);
        mVpBanner = findViewById(R.id.mVpBanner);
        mPicUrlList = new ArrayList<>(6);
        mCropImagePagerAdapter = new CenterCropImagePagerAdapter(mPicUrlList);
    }

    @Override
    protected void setup(Bundle savedInstanceState) {
        setSystemUiMode(SYSTEM_UI_MODE_TRANSPARENT_BAR_STATUS);
        setToolbarId(R.id.mDefaultToolbar, true);
        setTitle(null);

        fillTestData();

        mPageIndicator.setIndicatorColorSelected(Color.WHITE);
        mPageIndicator.setIndicatorColorUnselected(ContextCompat.getColor(this, R.color.backgroundColorWhiteLight));
        mPageIndicator.setIndicatorRadius((int) AndroidHelper.dip2px(3));
        mPageIndicator.setupWithViewPager(mVpBanner);

        mVpBanner.setAdapter(mCropImagePagerAdapter);

        mBtnConfirm.setOnClickListener(mOnConfirmClickListener);


        setupData();
    }

    private void fillTestData() {
        mPicUrlList.add(R.drawable.temp_image_1);
        mPicUrlList.add(R.drawable.temp_image_2);
        mPicUrlList.add(R.drawable.temp_image_3);
    }


    private void setupData() {
        String userId = getIntent().getStringExtra(INTENT_EXTRA_USER_ID);
        mUserProfile = getPresenter().getUserProfileFromLocal(userId);
        mContactOperationMessage = getPresenter().getContactOperationFromLocal(userId);
        if (mUserProfile == null) {
            finish();
            return;
        }
        if (mContactOperationMessage != null) {
            boolean isOperable = true;
            switch (mContactOperationMessage.getType()) {
                case REQUEST_ACTIVE:
                    mBtnConfirm.setText("等待对方同意");
                    isOperable = false;
                    break;
                case REJECT_ACTIVE:
                    mBtnConfirm.setText("已拒绝");
                    isOperable = false;
                    break;
                case REQUEST_PASSIVE:
                    mBtnConfirm.setText("同意");
                    break;
                case REJECT_PASSIVE:
                    mBtnConfirm.setText("对方拒绝添加");
                    isOperable = false;
                    break;
                default:
                    finish();
                    return;
            }

            mEtReason.setEnabled(false);
            mBtnConfirm.setEnabled(isOperable);
        } else {
            mBtnConfirm.setText("请求添加好友");
        }

        mEtReason.setText(getReason(mContactOperationMessage));
        mTvNickname.setText(mUserProfile.getNickname());
        mIvSexIcon.setSelected(mUserProfile.getSex() == UserProfile.Sex.FEMALE);
        StringBuilder locationAndAge = new StringBuilder();
        locationAndAge.append(UserDataFormatter.calculateAge(mUserProfile));
        if (!TextUtils.isEmpty(mUserProfile.getLocation())) {
            locationAndAge.append(" · ").append(mUserProfile.getLocation());
        }
        mTvLocationAndAge.setText(locationAndAge.toString());
        if (TextUtils.isEmpty(mUserProfile.getDescription())) {
            mTvSignature.setText(null);
            mTvSignature.setVisibility(View.GONE);
        } else {
            mTvSignature.setText(mUserProfile.getDescription());
            mTvSignature.setVisibility(View.VISIBLE);
        }
        GlideUtil.loadAvatarFromUrl(this, mIvAvatar, mUserProfile.getAvatar());

    }

    private String getReason(ContactOperationMessage message) {
        String reason = "";
        if (message != null) {
            switch (message.getType()) {
                case REQUEST_ACTIVE:
                case REQUEST_PASSIVE:
                    reason = mContactOperationMessage.getAddReason();
                    if (TextUtils.isEmpty(reason)) {
                        reason = "添加理由：无";
                    }
                    break;
                case REJECT_ACTIVE:
                case REJECT_PASSIVE:
                    reason = mContactOperationMessage.getRejectReason();
                    if (TextUtils.isEmpty(reason)) {
                        reason = "对方拒绝添加您为好友";
                    }
                    break;
            }
        }
        return reason;
    }

    private final View.OnClickListener mOnConfirmClickListener = new OnOnlySingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            String reason = mEtReason.getText().toString();
            if (mContactOperationMessage == null) {
                getPresenter().requestAddContact(mUserProfile.getUserId(), reason);
            } else {
                switch (mContactOperationMessage.getType()) {
                    case REQUEST_PASSIVE:
                        getPresenter().acceptAddContact(mUserProfile.getUserId());
                        break;
                    case REJECT_PASSIVE:
                        getPresenter().refusedAddContact(mUserProfile.getUserId(), reason);
                        break;
                }
            }
        }
    };

    @Override
    public void goBack() {
        finish();
    }
}
