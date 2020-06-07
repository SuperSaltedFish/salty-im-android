package me.zhixingye.salty.widget.adapter.holder;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.salty.protos.ContactProfile;
import com.salty.protos.ContactRemark;
import com.salty.protos.UserProfile;

import java.util.List;

import me.zhixingye.salty.R;
import me.zhixingye.salty.basic.BasicRecyclerViewAdapter;
import me.zhixingye.salty.tool.UserDataFormatter;
import me.zhixingye.salty.util.AndroidHelper;
import me.zhixingye.salty.util.GlideUtil;
import me.zhixingye.salty.widget.view.FlowLayout;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年06月06日.
 */
public class ContactHolder extends BasicRecyclerViewAdapter.BasicViewHolder<ContactProfile> {

    private TextView mTvName;
    private ImageView mIvAvatar;
    private ImageView mIvSex;
    private TextView mTvSignature;
    private TextView mTvAge;
    private FlowLayout mTagsFlowLayout;

    public ContactHolder(ViewGroup parent) {
        super(R.layout.item_contact, parent);
    }

    @Override
    protected void onCreateItemView(View itemView) {
        mTvName = itemView.findViewById(R.id.mTvName);
        mIvAvatar = itemView.findViewById(R.id.mIvAvatar);
        mIvSex = itemView.findViewById(R.id.mIvSex);
        mTagsFlowLayout = itemView.findViewById(R.id.mTagsFlowLayout);
        mTvSignature = itemView.findViewById(R.id.mTvSignature);
        mTvAge = itemView.findViewById(R.id.mTvAge);
        mTagsFlowLayout.setItemSpace((int) AndroidHelper.dip2px(4));
    }

    @Override
    protected void onBindData(ContactProfile data) {
        ContactRemark remark = data.getRemarkInfo();
        UserProfile user = data.getUserProfile();
        mTvName.setText(UserDataFormatter.getContactName(data));
        mIvSex.setSelected(user.getSex() == UserProfile.Sex.FEMALE);

        mTvAge.setText(UserDataFormatter.calculateAge(user));

        if (TextUtils.isEmpty(user.getDescription())) {
            mTvSignature.setText(null);
            mTvSignature.setVisibility(View.GONE);
        } else {
            mTvSignature.setText(user.getDescription());
            mTvSignature.setVisibility(View.VISIBLE);
        }

        mTagsFlowLayout.removeAllViews();
        List<String> tags = remark.getTagsList();
        if (tags != null && tags.size() != 0) {
            TextView label;
            for (String tag : tags) {
                label = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_label_small, mTagsFlowLayout, false);
                label.setText(tag);
                mTagsFlowLayout.addView(label);
            }
        }
        GlideUtil.loadAvatarFromUrl(mContext, mIvAvatar, user.getAvatar());
    }

    @Override
    protected void onRecycled() {
        GlideUtil.clear(mContext, mIvAvatar);
        mIvAvatar.setImageDrawable(null);
    }
}
