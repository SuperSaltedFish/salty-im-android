package me.zhixingye.salty.widget.adapter.holder;

import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;

import com.salty.protos.ContactOperationMessage;
import com.salty.protos.UserProfile;

import me.zhixingye.base.adapter.BasicListAdapterAdapter;
import me.zhixingye.base.listener.OnViewTouchClickListener;
import me.zhixingye.base.view.HexagonAvatarView;
import me.zhixingye.salty.R;
import me.zhixingye.salty.tool.UserDataFormatter;
import me.zhixingye.salty.util.GlideUtil;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年09月23日.
 */
public class ContactOperationMessageHolder extends BasicListAdapterAdapter.BasicViewHolder<ContactOperationMessage> {

    private ViewGroup mClRootView;
    private HexagonAvatarView mIvAvatar;
    private TextView mTvName;
    private ImageView mIvSex;
    private TextView mTvAge;
    private TextView mTvReason;
    private Button mTvPositive;
    private Button mTvNegative;

    private final DataAdapter mDataAdapter;

    private OnClickListener mOnClickListener;

    public ContactOperationMessageHolder(ViewGroup parent, @NonNull DataAdapter dataAdapter) {
        super(R.layout.item_contact_operation, parent);
        mDataAdapter = dataAdapter;
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

        mTvPositive.setOnClickListener(v -> {
            if (mOnClickListener != null) {
                mOnClickListener.onClickAccept(getBindingAdapterPosition());
            }
        });

        mTvNegative.setOnClickListener(v -> {
            if (mOnClickListener != null) {
                mOnClickListener.onClickRefused(getBindingAdapterPosition());
            }
        });

        mClRootView.setOnTouchListener(new OnViewTouchClickListener() {
            @Override
            protected void onClick(View v, int touchX, int touchY) {
                if (mOnClickListener != null) {
                    mOnClickListener.onClickItem(getBindingAdapterPosition());
                }
            }

            @Override
            protected void onLongClick(View v, int touchX, int touchY) {
                if (mOnClickListener != null) {
                    mOnClickListener.onLongClickItem(getBindingAdapterPosition(), v, touchX, touchY);
                }
            }
        });
    }

    @Override
    protected void onBindData(ContactOperationMessage data) {
        resetState();

        UserProfile userInfo = data.getTriggerProfile();
        mTvName.setText(userInfo.getNickname());
        mTvAge.setText(UserDataFormatter.calculateAge(userInfo));
        mIvSex.setSelected(userInfo.getSex() == UserProfile.Sex.FEMALE);
        GlideUtil.loadAvatarFromUrl(mContext, mIvAvatar, userInfo.getAvatar());

        switch (data.getType()) {
            case ACCEPT:
                setupAcceptState(data);
                break;
            case REJECT:
                setupRejectState(data);
                break;
            case REQUEST:
                setupRequestState(data);
                break;
            case DELETE:
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

    private void setupAcceptState(ContactOperationMessage data) {
        mTvPositive.setText("已添加好友");
        mTvPositive.setVisibility(View.VISIBLE);
        mTvPositive.setEnabled(false);
        mClRootView.setAlpha(0.4f);
    }


    private void setupRequestState(ContactOperationMessage data) {
        String reason = data.getAddReason();
        if (TextUtils.isEmpty(reason)) {
            reason = "无";
        }
        mTvReason.append("添加理由：");
        mTvReason.append(reason);
        mTvPositive.setVisibility(View.VISIBLE);

        UserProfile profile = data.getTriggerProfile();
        if (mDataAdapter.isMySelf(profile)) {
            mTvPositive.setEnabled(false);
            mTvPositive.setText("等待对方同意");
        } else {
            mTvPositive.setEnabled(true);
            mTvPositive.setText("添加");
        }
    }

    private void setupRejectState(ContactOperationMessage data) {
        String reason = data.getRejectReason();
        if (TextUtils.isEmpty(reason)) {
            reason = "无";
        }
        mTvReason.append("拒绝理由：");
        mTvReason.append(reason);
        mTvNegative.setVisibility(View.VISIBLE);

        UserProfile profile = data.getTriggerProfile();
        if (mDataAdapter.isMySelf(profile)) {
            mTvNegative.setEnabled(false);
            mTvNegative.setText("已拒绝添加");
        } else {
            mTvNegative.setEnabled(true);
            mTvNegative.setText("对方拒绝添加");
        }
    }

    private void setupDeleteState(ContactOperationMessage data) {
        String reason = data.getAddReason();
        if (TextUtils.isEmpty(reason)) {
            reason = "无";
        }
        mTvReason.append("添加理由：");
        mTvReason.append(reason);
        mTvNegative.setVisibility(View.VISIBLE);
        mTvNegative.setEnabled(false);
        mClRootView.setAlpha(0.4f);

        UserProfile profile = data.getTriggerProfile();
        if (mDataAdapter.isMySelf(profile)) {
            mTvNegative.setEnabled(false);
            mTvNegative.setText("已删除好友");
        } else {
            mTvNegative.setEnabled(true);
            mTvNegative.setText("对方已删除");
        }
    }

    public void setOnClickListener(OnClickListener listener) {
        mOnClickListener = listener;
    }

    public interface OnClickListener {
        void onClickAccept(int position);

        void onClickRefused(int position);

        void onClickItem(int position);

        void onLongClickItem(int position, View itemView, int touchX, int touchY);
    }

    public interface DataAdapter {
        boolean isMySelf(UserProfile profile);
    }
}
