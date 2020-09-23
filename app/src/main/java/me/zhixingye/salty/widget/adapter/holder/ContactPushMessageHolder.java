package me.zhixingye.salty.widget.adapter.holder;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.salty.protos.ContactPushMessage;
import com.salty.protos.UserProfile;

import me.zhixingye.salty.R;
import me.zhixingye.salty.basic.BasicListAdapterAdapter;
import me.zhixingye.salty.tool.UserDataFormatter;
import me.zhixingye.salty.util.GlideUtil;
import me.zhixingye.salty.widget.view.HexagonAvatarView;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年09月23日.
 */
public class ContactPushMessageHolder extends BasicListAdapterAdapter.BasicViewHolder<ContactPushMessage> {

    private ViewGroup mClRootView;
    private HexagonAvatarView mIvAvatar;
    private TextView mTvName;
    private ImageView mIvSex;
    private TextView mTvAge;
    private TextView mTvReason;
    private Button mTvPositive;
    private Button mTvNegative;


    private OnClickListener mOnClickListener;

    public ContactPushMessageHolder(ViewGroup parent) {
        super(R.layout.item_contact_operation, parent);
    }

    @Override
    protected void onCreateItemView(View itemView) {
        mClRootView = itemView.findViewById(R.id.mClRootView);
        mIvAvatar = itemView.findViewById(R.id.mIvAvatar);
        mTvName = itemView.findViewById(R.id.mTvName);
        mIvSex = itemView.findViewById(R.id.mIvSex);
        mTvAge = itemView.findViewById(R.id.mTvAge);
        mTvReason = itemView.findViewById(R.id.mTvReason);
        mTvPositive = itemView.findViewById(R.id.mTvPositive);
        mTvNegative = itemView.findViewById(R.id.mTvNegative);

        mTvPositive.setOnClickListener(mOnViewClickListener);
        mTvNegative.setOnClickListener(mOnViewClickListener);
        mClRootView.setOnClickListener(mOnViewClickListener);
    }

    @Override
    protected void onBindData(ContactPushMessage data) {
        resetState();

        UserProfile userInfo = data.getTriggerProfile();
        mTvName.setText(userInfo.getNickname());
        mTvAge.setText(UserDataFormatter.calculateAge(userInfo));
        mIvSex.setSelected(userInfo.getSex() == UserProfile.Sex.FEMALE);
        GlideUtil.loadAvatarFromUrl(mContext, mIvAvatar, userInfo.getAvatar());

        switch (data.getType()) {
            case ACCEPT_PASSIVE:
            case ACCEPT_ACTIVE:
                setupAcceptState(data);
                break;
            case REJECT_PASSIVE:
            case REJECT_ACTIVE:
                setupRejectState(data);
                break;
            case REQUEST_PASSIVE:
            case REQUEST_ACTIVE:
                setupRequestState(data);
                break;
            case DELETE_ACTIVE:
            case DELETE_PASSIVE:
                setupDeleteState(data);
                break;
        }
    }

    @Override
    protected void onRecycled() {
        GlideUtil.clear(mContext, mIvAvatar);
    }

    private void resetState() {
        mTvNegative.setVisibility(View.GONE);
        mTvPositive.setEnabled(true);
        mTvNegative.setVisibility(View.GONE);
        mTvPositive.setEnabled(true);
        mClRootView.setAlpha(1);
        mClRootView.setEnabled(true);
    }

    private void setupAcceptState(ContactPushMessage data) {
        mTvPositive.setText("已添加好友");
        mTvPositive.setVisibility(View.VISIBLE);
        mTvPositive.setEnabled(false);
        mClRootView.setAlpha(0.4f);
    }


    private void setupRequestState(ContactPushMessage data) {
        String reason = data.getAddReason();
        if (TextUtils.isEmpty(reason)) {
            reason = "无";
        }
        mTvReason.append("添加理由：");
        mTvReason.append(reason);
        mTvPositive.setVisibility(View.VISIBLE);
        switch (data.getType()) {
            case REQUEST_ACTIVE:
                mTvPositive.setEnabled(false);
                mTvPositive.setText("等待对方同意");
                break;
            case REQUEST_PASSIVE:
                mTvPositive.setEnabled(true);
                mTvPositive.setText("添加");
                break;
            default:
                break;
        }
    }

    private void setupRejectState(ContactPushMessage data) {
        String reason = data.getRejectReason();
        if (TextUtils.isEmpty(reason)) {
            reason = "无";
        }
        mTvReason.append("拒绝理由：");
        mTvReason.append(reason);
        mTvNegative.setVisibility(View.VISIBLE);
        mTvNegative.setEnabled(false);
        switch (data.getType()) {
            case REJECT_ACTIVE:
                mTvNegative.setText("已拒绝添加");
                break;
            case REJECT_PASSIVE:
                mTvNegative.setText("对方拒绝添加");
                break;
            default:
                break;
        }
    }

    private void setupDeleteState(ContactPushMessage data) {
        String reason = data.getAddReason();
        if (TextUtils.isEmpty(reason)) {
            reason = "无";
        }
        mTvReason.append("添加理由：");
        mTvReason.append(reason);
        mTvNegative.setVisibility(View.VISIBLE);
        mTvNegative.setEnabled(false);
        mClRootView.setAlpha(0.4f);
        switch (data.getType()) {
            case DELETE_ACTIVE:
                mTvNegative.setText("已删除好友");
                break;
            case DELETE_PASSIVE:
                mTvNegative.setText("对方已删除");
                break;
            default:
                break;
        }
    }

    public void setOnClickListener(OnClickListener listener) {
        mOnClickListener = listener;
    }

    private final View.OnClickListener mOnViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnClickListener == null) {
                return;
            }
            int position = getBindingAdapterPosition();
            switch (v.getId()) {
                case R.id.mTvPositive:
                    mOnClickListener.onClickAccept(position);
                    break;
                case R.id.mTvNegative:
                    mOnClickListener.onClickRefused(position);
                    break;
                case R.id.mClRootView:
                    mOnClickListener.onClickItem(position);
                    break;
            }
        }
    };

    public interface OnClickListener {
        void onClickAccept(int position);

        void onClickRefused(int position);

        void onClickItem(int position);
    }
}
